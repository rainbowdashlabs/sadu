/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.databases;

import de.chojo.sadu.jdbc.MySQLJdbc;

/**
 * Represents a SqLite database.
 */
public class MySql extends DefaultDatabase<MySQLJdbc> {

    private static final MySql type = new MySql();

    private MySql() {
    }

    /**
     * The MySQL type.
     *
     * @return database type
     */
    public static MySql mysql() {
        return type;
    }

    /**
     * The MySQL type.
     *
     * @return database type
     */
    public static MySql get() {
        return type;
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
