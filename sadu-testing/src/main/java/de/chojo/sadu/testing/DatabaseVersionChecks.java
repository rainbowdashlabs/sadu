/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.testing;

import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.file.Path;

import static de.chojo.sadu.testing.TestUtil.resourceContent;

/**
 * The DatabaseVersionChecks class provides methods for checking the content of the version file.
 */
public final class DatabaseVersionChecks {
    private DatabaseVersionChecks() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    /**
     * Checks if the version file exists in the "database" directory.
     * <p>
     * If the version file is missing, it throws an exception with an error message.
     */
    public static void assertVersionExists() {
        assertVersionExists(TestUtil.resourcePath());
    }

    /**
     * Checks if the version file exists in the "database" directory below the given resources directory.
     *
     * @param resources the resources directory that contains the database directory
     */
    static void assertVersionExists(Path resources) {
        ResourceChecks.assertResource(
                resources, "database/version", "Version file in directory database is missing (database/version)");
    }

    /**
     * Asserts that the version in the specified file is not blank.
     *
     * @throws IOException If an I/O error occurs.
     */
    public static void assertVersionNotBlank() throws IOException {
        assertVersionNotBlank(TestUtil.resourcePath());
    }

    /**
     * Asserts that the version file below the given resources directory is not blank.
     *
     * @param resources the resources directory that contains the database directory
     * @throws IOException If an I/O error occurs.
     */
    static void assertVersionNotBlank(Path resources) throws IOException {
        String content = resourceContent(resources, "database/version");
        Assertions.assertFalse(content.isBlank());
    }

    /**
     * Asserts that the format of a resource content matches the expected format.
     *
     * @throws IOException if there is an error reading the resource content
     */
    public static void assertFormat() throws IOException {
        assertFormat(TestUtil.resourcePath());
    }

    /**
     * Asserts that the version file below the given resources directory has the expected format.
     *
     * @param resources the resources directory that contains the database directory
     * @throws IOException if there is an error reading the version file
     */
    static void assertFormat(Path resources) throws IOException {
        assertFormat(resourceContent(resources, "database/version"));
    }

    /**
     * Asserts that the given version string follows the expected format {@code <major>.<patch>}.
     *
     * @param version the version string to be validated
     */
    public static void assertFormat(String version) {
        var split = version.split("\\.");
        Assertions.assertNotEquals(
                1,
                split.length,
                "Version does not contain a patch <major>.<patch> is expected. Got %s".formatted(version));
        Assertions.assertEquals(
                2, split.length, "Unexpected format detected. <major>.<patch> is expected. Got %s".formatted(version));
    }

    /**
     * Checks if the version exists, is not blank, and the format is correct.
     *
     * @throws IOException if an error occurs during the checking process
     */
    public static void all() throws IOException {
        assertVersionExists();
        assertVersionNotBlank();
        assertFormat();
    }
}
