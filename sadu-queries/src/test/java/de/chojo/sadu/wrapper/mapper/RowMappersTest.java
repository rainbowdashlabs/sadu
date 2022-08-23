/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.mapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class RowMappersTest {
    static RowMappers rowMappers = new RowMappers();

    @BeforeAll
    static void setUp() {
        rowMappers.register(Mapper.full, Mapper.sparse, Mapper.wildcard);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void wildcard() throws SQLException {
        var rowMapper = rowMappers.wildcard(Result.class);
        Assertions.assertTrue(rowMapper.isPresent());
    }

    @Test
    void find() throws SQLException {
        var rowMapper = rowMappers.find(Result.class, MetaResult.fullResultSet(), MapperConfig.DEFAULT);
        Assertions.assertTrue(rowMapper.isPresent());
        Assertions.assertEquals(Mapper.full, rowMapper.get());

        rowMapper = rowMappers.find(Result.class, MetaResult.sparseResultSet(), MapperConfig.DEFAULT);
        Assertions.assertTrue(rowMapper.isPresent());
        Assertions.assertEquals(Mapper.sparse, rowMapper.get());

        rowMapper = rowMappers.find(Result.class, MetaResult.aliasedResultSet(),
                new MapperConfig()
                        .addAlias("result", "r_result")
                        .strict());
        Assertions.assertTrue(rowMapper.isPresent());
        Assertions.assertEquals(Mapper.sparse, rowMapper.get());
    }
}
