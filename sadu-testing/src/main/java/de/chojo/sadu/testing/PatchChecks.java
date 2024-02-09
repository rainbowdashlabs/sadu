/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.testing;

import de.chojo.sadu.core.databases.Database;
import org.jetbrains.annotations.ApiStatus;
import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static de.chojo.sadu.testing.TestUtil.resourceContent;


/**
 * The PatchChecks class provides methods for checking the presence of patch files for the databases.
 */
public class PatchChecks {

    private PatchChecks() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    /**
     * Checks if files in the given databases match the specified base version.
     * Compares the file content of each database against the base version and throws an IOException if any mismatch is found.
     *
     * @param baseVersion The lowest major version of the databases.
     * @param databases   The databases to check.
     * @throws IOException If there is a mismatch between the file content in any of the databases and the base version.
     */
    public static void checkFiles(int baseVersion, Database<?, ?>... databases) throws IOException {
        var current = resourceContent("database/version");
        checkFiles(baseVersion, current, databases);
    }

    /**
     * Checks if the files for the given databases need to be updated based on the base version and current version.
     *
     * @param baseVersion    the base version of the databases
     * @param currentVersion the current version of the databases
     * @param databases      the databases to check
     * @throws IOException if an I/O error occurs while checking the files
     */
    @ApiStatus.Internal
    public static void checkFiles(int baseVersion, String currentVersion, Database<?, ?>... databases) throws IOException {
        checkFiles(baseVersion, currentVersion, Arrays.stream(databases).map(Database::name).toArray(String[]::new));
    }

    /**
     * Check the consistency of files in the given databases against a reference set of files.
     *
     * @param baseVersion    The base version of the database.
     * @param currentVersion The current version of the database.
     * @param databases      The names of the databases to check.
     * @throws IOException          If an I/O error occurs while accessing the files.
     * @throws AssertionFailedError If there are missing or unreachable files in the databases.
     */
    @ApiStatus.Internal
    public static void checkFiles(int baseVersion, String currentVersion, String... databases) throws IOException {
        // Map for all files present for a single database
        Map<String, List<Path>> databaseFiles = new HashMap<>();
        // All unique files in all databases
        Set<Path> refFiles = new HashSet<>();

        // collect all files from all databases
        for (var database : databases) {
            var dbBase = TestUtil.resourcePath().resolve("database/%s".formatted(database));
            try (var stream = Files.walk(dbBase)) {
                var files = stream.filter(p -> p.toFile().isFile()).map(dbBase::relativize).filter(p -> !p.toString().isBlank()).collect(Collectors.toList());
                refFiles.addAll(files);
                databaseFiles.put(database, files);
            }
        }

        // checking each database against the ref files and report differences
        for (var database : databaseFiles.keySet()) {
            var files = databaseFiles.get(database);
            if (refFiles.containsAll(files)) continue;
            var missing = new HashSet<>(refFiles);
            // Remove all existing files from the ref files
            files.forEach(missing::remove);
            var missingFiles = missing.stream().map(Path::toString).collect(Collectors.joining("\n"));
            throw new AssertionFailedError("There are missing files in database %s:%n%s".formatted(database, missingFiles));
        }

        // Check the ref files if there are all files that are expected to be there based on the current version.
        var current = Version.parse(currentVersion);
        var unreachable = new HashSet<>(refFiles);
        for (var major = baseVersion; major <= current.major; major++) {
            // check for setup file
            var setup = Path.of("%d/setup.sql".formatted(major));
            Assertions.assertTrue(unreachable.remove(setup), "Setup file missing at %s".formatted(setup));
            if (major != current.major) {
                // Check for migration file if there is a higher version
                Path migrate = Path.of("%d/migrate.sql".formatted(major));
                Assertions.assertTrue(unreachable.remove(migrate), "Migration file for migration from %d to %d missing at %s".formatted(major, major + 1, migrate));
            }
            if (major == current.major) {
                for (var patch = 1; patch <= current.patch; patch++) {
                    // Remove every patch until the current is reached
                    var patchFile = Path.of("%d/patch_%d.sql".formatted(major, patch));
                    Assertions.assertTrue(unreachable.remove(patchFile), "Patch file %s is missing".formatted(patchFile));
                }
            } else {
                // Just remove all patch files until none is found if this is not the latest version.
                // This will find patches which are not connected because of a missing patch in between.
                var patch = 1;
                var patchFile = Path.of("%d/patch_1.sql".formatted(major));
                while (unreachable.remove(patchFile)) {
                    patchFile = Path.of("%d/patch_%d.sql".formatted(major, ++patch));
                }
            }
        }

        var files = unreachable.stream().map(Path::toString).collect(Collectors.joining("\n"));
        Assertions.assertTrue(unreachable.isEmpty(), "There are unreachable files:%n%s".formatted(files));
    }

    private static class Version {
        final int major, patch;

        Version(int major, int patch) {
            this.major = major;
            this.patch = patch;
        }

        static Version parse(String version) {
            var split = version.split("\\.");
            return new Version(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        }
    }
}
