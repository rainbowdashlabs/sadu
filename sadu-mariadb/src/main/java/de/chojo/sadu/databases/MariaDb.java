/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.databases;

import de.chojo.sadu.jdbc.MariaDbJdbc;

public class MariaDb extends DefaultType<MariaDbJdbc> {

    private static final MariaDb type = new MariaDb();

    public static MariaDb mariadb() {
        return type;
    }
    public static MariaDb get() {
        return type;
    }

    private MariaDb() {
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
