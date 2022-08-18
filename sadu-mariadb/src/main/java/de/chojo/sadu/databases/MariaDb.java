/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.databases;

import de.chojo.sadu.jdbc.MariaDbJdbc;

/**
 * Represents a MariaDb database.
 */
public class MariaDb extends DefaultDatabase<MariaDbJdbc> {

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
        return cleanStatements(queries.split(";"));
    }


    @Override
    public MariaDbJdbc jdbcBuilder() {
        return new MariaDbJdbc();
    }
}
