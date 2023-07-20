/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.mapper;

import de.chojo.sadu.mapper.MapperConfig;
import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.mapper.rowmapper.RowMapper;
import de.chojo.sadu.wrapper.QueryBuilder;
import de.chojo.sadu.wrapper.util.Row;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RowMapperTest {

    @Test
    void map() throws SQLException {
        var map = Mapper.sparse.map(new Row(MetaResult.sparseResultSet(), MapperConfig.DEFAULT));
        assertEquals(Result.class, map.getClass(), "Polimorphism check failed.");
        assertEquals(new Result(1, "result"), map);

        map = Mapper.full.map(new Row(MetaResult.fullResultSet(), MapperConfig.DEFAULT));
        assertEquals(MetaResult.class, map.getClass(), "Polimorphism check failed.");
        assertEquals(new MetaResult(1, "result", "meta"), map);

        map = Mapper.full.map(new Row(MetaResult.aliasedResultSet(), new MapperConfig().addAlias("result", "r_result").strict()));
        assertEquals(MetaResult.class, map.getClass(), "Polimorphism check failed.");
        assertEquals(new Result(1, "result"), map);
    }

    @Test
    void isWildcard() {
        assertTrue(Mapper.wildcard.isWildcard());
        assertFalse(Mapper.sparse.isWildcard());
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
        assertEquals(0, Mapper.sparse.applicable(resultSet, new MapperConfig().strict()));
        // Mapper is applicable
        assertEquals(3, Mapper.full.applicable(resultSet, new MapperConfig().strict()));
        // Check non strict mode. Mapper can apply two fields
        assertEquals(2, Mapper.sparse.applicable(resultSet));
        assertEquals(3, Mapper.full.applicable(resultSet));
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
        RowMapperRegistry rowMapperRegistry = new RowMapperRegistry().register(fullMapper, sparseMapper);

// Retrieves a list of MetaResults
        List<Result> metaResults = QueryBuilder.builder(source, Result.class)
                .defaultConfig(config -> config.rowMappers(rowMapperRegistry))
                .query("SELECT id, result, meta FROM results")
                .emptyParams()
                // Call map instead of read rows. This will let the query builder determine the type by itself.
                .map()
                .allSync();


// Retrieves a list of Results
        List<Result> results = QueryBuilder.builder(source, Result.class)
                .defaultConfig(config -> config.rowMappers(rowMapperRegistry))
                .query("SELECT id, result FROM results")
                .emptyParams()
                // Call map instead of read rows. This will let the query builder determine the type by itself.
                .map()
                .allSync();

// Allows setting of an alias for a column without the need of creating a new mapper
        results = QueryBuilder.builder(source, Result.class)
                .defaultConfig(config -> config.rowMappers(rowMapperRegistry))
                .query("SELECT id, result as r_result FROM results")
                .emptyParams()
                // Call map instead of read rows. We map the column result to r_result when it gets requested.
                .map(new MapperConfig().addAlias("result", "r_result").strict())
                .allSync();
    }
}
