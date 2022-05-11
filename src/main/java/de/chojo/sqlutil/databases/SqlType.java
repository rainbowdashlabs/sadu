/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil.databases;

import de.chojo.sqlutil.jdbc.JdbcConfig;
import de.chojo.sqlutil.jdbc.MariaDbJdbc;
import de.chojo.sqlutil.jdbc.MySQLJdbc;
import de.chojo.sqlutil.jdbc.PostgresJdbc;
import de.chojo.sqlutil.jdbc.SqLiteJdbc;

import java.util.Arrays;

/**
 * Defines a sql type and handles RDBMS specific actions.
 *
 * @param <T> type of the database defined by the {@link SqlType}
 */
public interface SqlType<T extends JdbcConfig<?>> {

    /**
     * The PostgreSQL type.
     */
    SqlType<PostgresJdbc> POSTGRES = new Postgres();
    /**
     * The MariaDb type.
     */
    SqlType<MariaDbJdbc> MARIADB = new MariaDb();

    /**
     * The SqLite type.
     */

    SqlType<SqLiteJdbc> SQLITE = new SqLite();

    /**
     * The MySQL type.
     */
    SqlType<MySQLJdbc> MYSQL = new MySql();

    /**
     * Creates a query to create a version table on the database.
     * <p>
     * The query needs to handle errors when the table exists.
     *
     * @param table table name
     * @return query to create a version table
     */
    String createVersionTableQuery(String table);

    /**
     * Gets a query to read the version from the version table
     *
     * @param table table name
     * @return query to read the version table
     */
    String getVersion(String table);

    /**
     * Get a unique name to identify the database.
     *
     * @return database name
     */
    String getName();

    /**
     * Creates a query to insert a version into the version table.
     * <p>
     * Versions are always inserted and not updated.
     *
     * @param table table name
     * @return query to insert the version.
     */
    String insertVersion(String table);

    /**
     * Creates a query to delete all entries from the version table.
     *
     * @param table table name
     * @return query to clear version table
     */
    String deleteVersion(String table);

    /**
     * Returns a query which returns a boolean indicating if a schema with this name exists.
     *
     * @return query to check for schema
     */
    default String schemaExists() {
        if (hasSchemas()) {
            throw new RuntimeException("schemas are supported but not implemented");
        }
        return "";
    }

    ;

    /**
     * Returns a query to create a schema with this name.
     *
     * @param schema schema
     * @return query to create schema
     */
    default String createSchema(String schema) {
        if (hasSchemas()) {
            throw new RuntimeException("schemas are supported but not implemented");
        }
        return "";
    }

    ;

    /**
     * Returns the {@link JdbcConfig} implementation for this database
     *
     * @return jdbc builder
     */
    T jdbcBuilder();

    /**
     * If the db does not allow to execute multiple queries this function should return every query in one string
     *
     * @param queries queries
     * @return splitted queries if needed
     */
    default String[] splitStatements(String queries) {
        return new String[]{queries};
    }

    /**
     * Function supposed to clean statements by removing empty statements
     *
     * @param queries queries
     * @return a new array
     */
    default String[] cleanStatements(String[] queries) {
        return Arrays.stream(queries).filter(query -> !query.isBlank()).toArray(String[]::new);
    }

    /**
     * Indicates if this type supports schemas.
     * <p>
     * When schemas are supported the methods {@link #createSchema(String)} and {@link #schemaExists()} needs to be implemented.
     *
     * @return true when schemas are supported
     */
    default boolean hasSchemas() {
        return false;
    }
}
