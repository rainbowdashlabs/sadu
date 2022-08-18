/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.databases;

import de.chojo.sadu.jdbc.MySQLJdbc;

public class MySql extends DefaultType<MySQLJdbc> {

    private static final MySql type = new MySql();

    public static MySql mysql() {
        return type;
    }
    public static MySql get() {
        return type;
    }

    private MySql() {
    }

    @Override
    public String name() {
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
