/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.mapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RowMapperRegistryTest {
    static RowMapperRegistry rowMapperRegistry = new RowMapperRegistry();

    @BeforeAll
    static void setUp() {
        rowMapperRegistry.register(Mapper.full, Mapper.sparse, Mapper.wildcard);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void wildcard() throws SQLException {
        var rowMapper = rowMapperRegistry.wildcard(Result.class);
        assertTrue(rowMapper.isPresent());
    }

    @Test
    void find() throws SQLException {
        var rowMapper = rowMapperRegistry.find(Result.class, MetaResult.fullResultSet(), MapperConfig.DEFAULT);
        assertTrue(rowMapper.isPresent());
        assertEquals(Mapper.full, rowMapper.get());

        rowMapper = rowMapperRegistry.find(Result.class, MetaResult.sparseResultSet(), MapperConfig.DEFAULT);
        assertTrue(rowMapper.isPresent());
        assertEquals(Mapper.sparse, rowMapper.get());

        rowMapper = rowMapperRegistry.find(Result.class, MetaResult.aliasedResultSet(),
                new MapperConfig()
                        .addAlias("result", "r_result")
                        .strict());
        assertTrue(rowMapper.isPresent());
        assertEquals(Mapper.sparse, rowMapper.get());
    }
}
