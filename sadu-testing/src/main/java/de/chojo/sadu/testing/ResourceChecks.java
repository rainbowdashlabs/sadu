/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.testing;

import org.junit.jupiter.api.Assertions;

import java.nio.file.Path;

import static de.chojo.sadu.testing.TestUtil.resourcePath;

/**
 * The ResourceChecks class provides utility methods for checking the existence of resources.
 */
public class ResourceChecks {
    private ResourceChecks() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    /**
     * Asserts that a resource file exists at the specified path.
     *
     * @param path The path to the resource file.
     * @throws AssertionError If the resource file does not exist.
     */
    public static void assertResource(String path) {
        assertResource(path, "Missing file at \"%s\".".formatted(path));
    }

    /**
     * Asserts the existence of a resource at the specified path.
     *
     * @param path    the path of the resource to be verified
     * @param message the error message to be displayed if the resource does not exist
     */
    public static void assertResource(String path, String message) {
        assertResource(resourcePath(), path, message);
    }

    /**
     * Asserts the existence of a resource below the given resources directory.
     *
     * @param resources the resources directory
     * @param path      the path of the resource, relative to the resources directory
     * @param message   the error message to be displayed if the resource does not exist
     */
    static void assertResource(Path resources, String path, String message) {
        Assertions.assertTrue(resources.resolve(path).toFile().exists(), message);
    }
}
