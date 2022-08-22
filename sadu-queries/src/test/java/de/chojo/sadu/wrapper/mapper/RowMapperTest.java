/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.mapper;

import de.chojo.sadu.wrapper.util.Row;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RowMapperTest {

    @Test
    void map() throws SQLException {
        var map =Mapper.sparse.map(new Row(Result.sparseResultSet()));
        Assertions.assertEquals(map, new Result(1, "result"));

        map =Mapper.sparse.map(new Row(Result.fullResultSet()));
        Assertions.assertEquals(map, new Result(1, "result", "meta"));
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

        Assertions.assertEquals(2, Mapper.sparse.applicable(resultSet));
        Assertions.assertEquals(3, Mapper.full.applicable(resultSet));
    }


}
