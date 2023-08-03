/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestUtil {
private TestUtil() {
    throw new UnsupportedOperationException("This is a utility class.");
}

    public static String resourceContent(String path) throws IOException {
        return Files.readString(resourcePath().resolve(path));
    }

    public static Path resourcePath() {
        return Path.of("src/main/resources");
    }
}
