/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jdbc;

import de.chojo.sadu.databases.MariaDb;
import de.chojo.sadu.mapper.MariaDbMapper;
import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.wrapper.QueryBuilderConfig;
import org.junit.jupiter.api.BeforeEach;

class MariaDbJdbcTest {

    MariaDbJdbc jdbc;

    @BeforeEach
    void setUp() {
        jdbc = MariaDb.mariadb().jdbcBuilder();
    }


}
