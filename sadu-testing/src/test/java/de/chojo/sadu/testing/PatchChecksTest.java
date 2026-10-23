/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.testing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.opentest4j.AssertionFailedError;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class PatchChecksTest {

    @Test
    void checkFiles() throws IOException {
        Assertions.assertDoesNotThrow(() -> PatchChecks.checkFiles(TestResources.PATH, 1, "2.1", "postgresql"));
        Assertions.assertThrows(
                AssertionFailedError.class, () -> PatchChecks.checkFiles(TestResources.PATH, 1, "2.2", "postgresql"));
    }

    @Test
    void reportsFileMissingForOneDatabase(@TempDir Path resources) throws IOException {
        createFiles(resources.resolve("database/postgresql"), "1/setup.sql", "1/patch_1.sql");
        createFiles(resources.resolve("database/mariadb"), "1/setup.sql");

        var error = Assertions.assertThrows(
                AssertionFailedError.class, () -> PatchChecks.checkFiles(resources, 1, "1.1", "postgresql", "mariadb"));
        Assertions.assertTrue(error.getMessage().contains("mariadb"));
    }

    @Test
    void acceptsSameFilesForAllDatabases(@TempDir Path resources) throws IOException {
        createFiles(resources.resolve("database/postgresql"), "1/setup.sql", "1/patch_1.sql");
        createFiles(resources.resolve("database/mariadb"), "1/setup.sql", "1/patch_1.sql");

        Assertions.assertDoesNotThrow(() -> PatchChecks.checkFiles(resources, 1, "1.1", "postgresql", "mariadb"));
    }

    private static void createFiles(Path base, String... files) throws IOException {
        for (var file : files) {
            var path = base.resolve(file);
            Files.createDirectories(path.getParent());
            Files.writeString(path, "SELECT 1;");
        }
    }
}
