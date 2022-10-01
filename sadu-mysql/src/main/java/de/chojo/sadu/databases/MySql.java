/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.databases;

import de.chojo.sadu.updater.UpdaterBuilder;
import de.chojo.sadu.jdbc.MySQLJdbc;
import de.chojo.sadu.updater.BaseSqlUpdaterBuilder;

/**
 * Represents a SqLite database.
 */
public class MySql extends DefaultDatabase<MySQLJdbc, BaseSqlUpdaterBuilder<MySQLJdbc, ?>> {

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

    @Override
    public UpdaterBuilder<MySQLJdbc, BaseSqlUpdaterBuilder<MySQLJdbc, ?>> newSqlUpdaterBuilder() {
        return new BaseSqlUpdaterBuilder<>(this);
    }
}
