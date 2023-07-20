/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.mapper;

import de.chojo.sadu.mapper.MapperConfig;
import de.chojo.sadu.mapper.RowMapperRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
