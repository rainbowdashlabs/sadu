/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil.jdbc;

import de.chojo.sqlutil.databases.SqlType;
import org.junit.jupiter.api.BeforeEach;

class MariaDbJdbcTest {

    MariaDbJdbc jdbc;

    @BeforeEach
    void setUp() {
        jdbc = SqlType.MARIADB.jdbcBuilder();
    }




}
