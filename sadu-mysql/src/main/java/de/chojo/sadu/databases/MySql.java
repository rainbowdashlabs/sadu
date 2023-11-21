/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.databases;

import de.chojo.sadu.updater.StatementSplitter;
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
        return StatementSplitter.split(queries);
    }

    @Override
    public UpdaterBuilder<MySQLJdbc, BaseSqlUpdaterBuilder<MySQLJdbc, ?>> newSqlUpdaterBuilder() {
        return new BaseSqlUpdaterBuilder<>(this);
    }
}
