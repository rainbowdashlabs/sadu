/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper;

import de.chojo.sadu.base.QueryFactory;
import de.chojo.sadu.databases.MariaDb;
import de.chojo.sadu.wrapper.QueryBuilder;
import de.chojo.sadu.wrapper.QueryBuilderConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MariaDbMapperTest {

    @Test
    //@Disabled
    public void test() throws SQLException {
        MariaDbDataSource dataSource = new MariaDbDataSource(MariaDb.get().jdbcBuilder().login("root", "root").jdbcUrl());

        RowMapperRegistry registry = new RowMapperRegistry().register(MariaDbMapper.getDefaultMapper());

        QueryBuilderConfig.setDefault(QueryBuilderConfig.builder().rowMappers(registry).build());

        QueryFactory queryFactory = new QueryFactory(dataSource);

        Optional<Integer> integer = queryFactory.builder(Integer.class)
                .query("SELECT 1")
                .emptyParams()
                .map()
                .firstSync();

        assertTrue(integer.isPresent());

        Optional<Boolean> bool = queryFactory.builder(Boolean.class)
                .query("SELECT TRUE")
                .emptyParams()
                .map()
                .firstSync();

        assertTrue(bool.isPresent());
    }

}
