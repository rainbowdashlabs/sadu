/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.mapper;

import de.chojo.sadu.wrapper.QueryBuilder;
import de.chojo.sadu.wrapper.mapper.rowmapper.RowMapper;
import de.chojo.sadu.wrapper.util.Row;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RowMapperTest {

    @Test
    void map() throws SQLException {
        var map = Mapper.sparse.map(new Row(MetaResult.sparseResultSet()));
        Assertions.assertEquals(Result.class, map.getClass(), "Polimorphism check failed.");
        Assertions.assertEquals(new Result(1, "result"), map);

        map = Mapper.full.map(new Row(MetaResult.fullResultSet()));
        Assertions.assertEquals(MetaResult.class, map.getClass(), "Polimorphism check failed.");
        Assertions.assertEquals(new MetaResult(1, "result", "meta"), map);
    }

    @Test
    void isWildcard() {
        Assertions.assertTrue(Mapper.wildcard.isWildcard());
        Assertions.assertFalse(Mapper.sparse.isWildcard());
    }

    @Test
    void applicable() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);
        when(meta.getColumnCount()).thenReturn(3);
        when(meta.getColumnLabel(1)).thenReturn("id");
        when(meta.getColumnLabel(2)).thenReturn("result");
        when(meta.getColumnLabel(3)).thenReturn("meta");
        when(resultSet.getMetaData()).thenReturn(meta);

        // Check strict mode.
        // Mapper is not applicable since we would loose the meta column
        Assertions.assertEquals(0, Mapper.sparse.applicable(resultSet, true));
        // Mapper is applicable
        Assertions.assertEquals(3, Mapper.full.applicable(resultSet, true));
        // Check non strict mode. Mapper can apply two fields
        Assertions.assertEquals(2, Mapper.sparse.applicable(resultSet));
        Assertions.assertEquals(3, Mapper.full.applicable(resultSet));
    }

    void syntaxExample() {
        DataSource source = null;

// Create a row mapper for the Result class with three different colums
RowMapper<Result> fullMapper = RowMapper.forClass(Result.class)
                                        // Define how the row should be mapped
                                        .mapper(row -> new MetaResult(row.getInt("id"),
                                                row.getString("result"),
                                                row.getString("meta")))
                                        // define the column names
                                        .addColumns("id", "result", "meta")
                                        .build();

// Allows polymorphism
RowMapper<Result> sparseMapper = RowMapper.forClass(Result.class)
                                          // Define how the row should be mapped
                                          .mapper(row -> new Result(row.getInt("id"),
                                                  row.getString("result")))
                                          // define the column names
                                          .addColumns("id", "result")
                                          .build();

// Register the mapper
RowMappers rowMappers = new RowMappers().register(fullMapper, sparseMapper);

// Retrieves a list of MetaResults
List<Result> metaResults = QueryBuilder.builder(source, Result.class)
        .defaultConfig(config -> config.rowMappers(rowMappers))
        .query("SELECT id, result, meta FROM results")
        .emptyParams()
        // Call map instead of read rows. This will let the query builder determine the type by itself.
        .map()
        .allSync();


// Retrieves a list of Results
List<Result> results = QueryBuilder.builder(source, Result.class)
        .defaultConfig(config -> config.rowMappers(rowMappers))
        .query("SELECT id, result FROM results")
        .emptyParams()
        // Call map instead of read rows. This will let the query builder determine the type by itself.
        .map()
        .allSync();
    }
}
