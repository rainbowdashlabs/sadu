/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil.databases;

import de.chojo.sqlutil.jdbc.MySQLJdbc;

public class MySql extends DefaultType<MySQLJdbc> {

    @Override
    public String getName() {
        return "mysql";
    }

    @Override
    public MySQLJdbc jdbcBuilder() {
        return new MySQLJdbc();
    }

    @Override
    public String[] splitStatements(String queries) {
        return cleanStatements(queries.split(";"));
    }
}
