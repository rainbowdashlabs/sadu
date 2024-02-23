/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.testing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestUtil {
    /**
     * The resourcePath variable represents the path to the main resources directory.
     * It is a static Path object that points to the "src/main/resources" directory.
     * <p>
     * This variable is used to access resources, such as configuration files or other assets,
     * that are included in the project. By using a predefined path, it ensures consistent
     * access to the resources from various parts of the codebase.
     * <p>
     * The resourcePath can be modified to point to a different resources directory if needed.
     *
     * @see Path
     */
    public static final Path resourcePath = Path.of("src/main/resources");

    private TestUtil() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    /**
     * Returns the content of a resource file located at the given path.
     *
     * @param path the path of the resource file
     * @return the content of the resource file as a string
     * @throws IOException if an I/O error occurs while reading the file
     */
    public static String resourceContent(String path) throws IOException {
        return Files.readString(resourcePath().resolve(path));
    }

    /**
     * Returns the resource path.
     *
     * @return Path representing the resource path.
     */
    public static Path resourcePath() {
        return resourcePath;
    }
}
