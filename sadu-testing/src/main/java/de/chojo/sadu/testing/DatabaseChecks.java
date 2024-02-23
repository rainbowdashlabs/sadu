/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.testing;

import de.chojo.sadu.core.databases.Database;

/**
 * Class to check for existence of databases.
 */
public class DatabaseChecks {
    private DatabaseChecks() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    /**
     * Asserts the existence of the specified database.
     *
     * @param database the database to be checked for existence
     */
    public static void assertDatabase(String database) {
        String path = "database/%s/".formatted(database);
        ResourceChecks.assertResource(path, "Directory for database %s is missing. Checked for directory: %s".formatted(database, path));
    }

    /**
     * Asserts the existence of the specified databases.
     *
     * @param databases the databases to be checked for existence
     */
    public static void assertDatabase(Database<?, ?>... databases) {
        for (var database : databases) {
            assertDatabase(database.name());
        }
    }

    /**
     * Asserts the existence of the specified databases.
     *
     * @param databases the names of the databases to be checked for existence
     */
    public static void assertDatabase(String... databases) {
        for (var database : databases) {
            assertDatabase(database);
        }
    }
}
