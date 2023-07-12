/*
 *     SPDX-License-Identifier: LGPL-3.0-only
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.databases;

import de.chojo.sadu.updater.UpdaterBuilder;
import de.chojo.sadu.databases.exceptions.NotImplementedException;
import de.chojo.sadu.databases.exceptions.NotSupportedException;
import de.chojo.sadu.jdbc.JdbcConfig;
import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;

/**
 * Defines a sql type and handles RDBMS specific actions.
 * <p>
 * Every type should implement two static methods, which provide the driver by name and one by get.
 *
 * <pre>{@code
 * class MySqlType extends SqlType<MySqlTypeJdbc> {
 *      private static final MySqlType type = MySqlType();
 *
 *      public static MySqlType mysqltype() {
 *          return type;
 *      }
 *
 *      public static MySqlType get() {
 *          return type;
 *      }
 *
 *      private MySqlType(){}
 * }
 * }</pre>
 *
 * @param <T> type of the database defined by the {@link Database}
 * @param <U> type of the BaseSqlUpdater
 */
public interface Database<T extends JdbcConfig<?>, U extends UpdaterBuilder<T, ?>> {

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
    String versionQuery(String table);

    /**
     * Get a unique name to identify the database.
     *
     * @return database name
     */
    String name();

    /**
     * Get the alias for the database name.
     *
     * @return array of aliases
     */
    default String[] alias() {
        return new String[0];
    }

    /**
     * Checks if the given name matches the {@link #name()} or any {@link #alias()}. case-insensitive.
     *
     * @param name name to check
     * @return true if any match was found
     */
    default boolean matches(String name) {
        if (name.equalsIgnoreCase(name())) {
            return true;
        }
        for (var alias : alias()) {
            if (alias.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

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
            throw new NotImplementedException("Schemas are supported but not implemented");
        }
        throw new NotSupportedException("Schemas are not supported.");
    }

    /**
     * Returns a query to create a schema with this name.
     *
     * @param schema schema
     * @return query to create schema
     */
    default String createSchema(String schema) {
        if (hasSchemas()) {
            throw new NotImplementedException("Schemas are supported but not implemented");
        }
        throw new NotSupportedException("Schemas are not supported.");
    }

    /**
     * Returns the {@link JdbcConfig} implementation for this database
     *
     * @return jdbc builder
     */
    T jdbcBuilder();

    /**
     * The split queries, if the db does not allow to execute multiple queries this function should return every query in one string.
     * <p>
     * Otherwise, this array will only contain one entry.
     *
     * @param queries queries
     * @return queries
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

    /**
     * Instantiates an implementation of {@link UpdaterBuilder}
     * @return the instance
     */
    @ApiStatus.Internal
    UpdaterBuilder<T, U> newSqlUpdaterBuilder();
}
