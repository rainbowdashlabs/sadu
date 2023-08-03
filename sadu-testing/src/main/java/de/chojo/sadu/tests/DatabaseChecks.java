/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.tests;

import de.chojo.sadu.databases.Database;
import org.junit.jupiter.api.Assertions;

public class DatabaseChecks {
    private DatabaseChecks() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    public static void assertDatabase(int major, String database) {
        String path = "database/%s/".formatted(database);
        ResourceChecks.assertResource(path, "Directory for database %s is missing. Checked for directory: %s".formatted(database, path));
    }

    public static void assertDatabase(int major, Database<?, ?>... databases) {
        for (var database : databases) {
            assertDatabase(major, database.name());
        }
    }

    public static void assertDatabase(int major, String... databases) {
        for (var database : databases) {
            assertDatabase(major, database);
        }
    }
}
