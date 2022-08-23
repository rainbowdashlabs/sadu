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

    @BeforeAll
    static void setUp() {
        RowMappers.register(Mapper.full);
        RowMappers.register(Mapper.sparse);
        RowMappers.register(Mapper.wildcard);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void wildcard() throws SQLException {
        var rowMapper = RowMappers.wildcard(Result.class);
        Assertions.assertTrue(rowMapper.isPresent());
    }

    @Test
    void find() throws SQLException {
        var rowMapper = RowMappers.find(Result.class, Result.fullResultSet());
        Assertions.assertTrue(rowMapper.isPresent());
        Assertions.assertEquals(Mapper.full, rowMapper.get());

        rowMapper = RowMappers.find(Result.class, Result.sparseResultSet());
        Assertions.assertTrue(rowMapper.isPresent());
        Assertions.assertEquals(Mapper.sparse, rowMapper.get());
    }
}
