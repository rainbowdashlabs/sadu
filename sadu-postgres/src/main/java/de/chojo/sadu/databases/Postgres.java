/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.databases;

import de.chojo.sadu.jdbc.PostgresJdbc;

public class Postgres extends DefaultType<PostgresJdbc> {

    private static final Postgres type = new Postgres();

    public static Postgres postgres() {
        return type;
    }


    @Override
    public String getName() {
        return "postgres";
    }

    @Override
    public PostgresJdbc jdbcBuilder() {
        return new PostgresJdbc();
    }

    @Override
    public boolean hasSchemas() {
        return true;
    }

    @Override
    public String schemaExists() {
        return """
                SELECT EXISTS (
                   SELECT FROM information_schema.tables
                   WHERE  table_schema = ?
                   );
                """;
    }

    @Override
    public String createSchema(String schema) {
        return String.format("CREATE SCHEMA IF NOT EXISTS %s;", schema);
    }
}
