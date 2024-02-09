/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mariadb.databases;

import de.chojo.sadu.core.databases.DefaultDatabase;
import de.chojo.sadu.core.updater.StatementSplitter;
import de.chojo.sadu.core.updater.UpdaterBuilder;
import de.chojo.sadu.mariadb.jdbc.MariaDbJdbc;
import de.chojo.sadu.updater.BaseSqlUpdaterBuilder;

/**
 * Represents a MariaDb database.
 */
public class MariaDb extends DefaultDatabase<MariaDbJdbc, BaseSqlUpdaterBuilder<MariaDbJdbc, ?>> {

    private static final MariaDb type = new MariaDb();

    private MariaDb() {
    }

    /**
     * The MariaDb type.
     *
     * @return database type
     */
    public static MariaDb mariadb() {
        return type;
    }

    /**
     * The MariaDb type.
     *
     * @return database type
     */
    public static MariaDb get() {
        return type;
    }

    @Override
    public String name() {
        return "mariadb";
    }

    @Override
    public String[] splitStatements(String queries) {
        return StatementSplitter.split(queries);
    }

    @Override
    public UpdaterBuilder<MariaDbJdbc, BaseSqlUpdaterBuilder<MariaDbJdbc, ?>> newSqlUpdaterBuilder() {
        return new BaseSqlUpdaterBuilder<>(this);
    }


    @Override
    public MariaDbJdbc jdbcBuilder() {
        return new MariaDbJdbc();
    }
}
