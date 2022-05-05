/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil.databases;

import de.chojo.sqlutil.jdbc.MariaDbJdbc;

public class MariaDb extends DefaultType<MariaDbJdbc> {

    @Override
    public String getName() {
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
