/*
 *     SPDX-License-Identifier: LGPL-3.0-only
 *
 *     Copyright (C) RainbowDashLabs and Contributor
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
