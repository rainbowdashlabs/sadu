/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil.databases;

import de.chojo.sqlutil.jdbc.PostgresJdbc;

public class Postgres extends DefaultType<PostgresJdbc> {

    @Override
    public String getName() {
        return "postgres";
    }

    @Override
    public PostgresJdbc jdbcBuilder() {
        return new PostgresJdbc();
    }
}
