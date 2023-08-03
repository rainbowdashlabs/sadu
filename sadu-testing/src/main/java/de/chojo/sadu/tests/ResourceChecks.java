/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.tests;

import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static de.chojo.sadu.tests.TestUtil.resourcePath;

public class ResourceChecks {
    private ResourceChecks() {
        throw new UnsupportedOperationException("This is a utility class.");
    }


    public static void assertResource(String path) {
        assertResource(path, "Missing file at \"%s\".".formatted(path));
    }

    public static void assertResource(String path, String message) {
        Assertions.assertTrue(resourcePath().resolve(path).toFile().exists(), message);
    }

}
