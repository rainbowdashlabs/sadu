/*
 *     SPDX-License-Identifier: LGPL-3.0-only
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.databases;

import de.chojo.sadu.jdbc.PostgreSqlJdbc;
import de.chojo.sadu.update.PostgreSqlUpdaterBuilder;
import de.chojo.sadu.updater.BaseSqlUpdaterBuilder;

/**
 * Represents a PostgreSQL database.
 */
public class PostgreSql extends DefaultDatabase<PostgreSqlJdbc, PostgreSqlUpdaterBuilder> {

    private static final PostgreSql type = new PostgreSql();

    private PostgreSql() {
    }

    /**
     * The PostgreSQL type.
     *
     * @return database type
     */
    public static PostgreSql postgresql() {
        return type;
    }

    /**
     * The PostgreSQL type.
     *
     * @return database type
     */
    public static PostgreSql get() {
        return type;
    }

    @Override
    public String name() {
        return "postgresql";
    }

    @Override
    public String[] alias() {
        return new String[]{"postgres"};
    }

    @Override
    public PostgreSqlJdbc jdbcBuilder() {
        return new PostgreSqlJdbc();
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

    @Override
    public BaseSqlUpdaterBuilder<PostgreSqlJdbc, PostgreSqlUpdaterBuilder> newSqlUpdaterBuilder() {
        return new PostgreSqlUpdaterBuilder(this);
    }
}
