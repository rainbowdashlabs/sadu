/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.tests;

import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.io.InputStream;

import static de.chojo.sadu.tests.TestUtil.resourceContent;

public class DatabaseVersionChecks {
    public static void assertVersionExists() {
        ResourceChecks.assertResource("database/version", "Version file in directory database is missing (database/version)");
    }

    public static void assertVersionNotBlank() throws IOException {
        String content = resourceContent("database/version");
        Assertions.assertFalse(content.isBlank());
    }


    public static void assertFormat() throws IOException {
        assertFormat(resourceContent("database/version"));
    }
    public static void assertFormat(String version) throws IOException {
        String[] split = version.split("\\.");
        Assertions.assertNotEquals(1, split.length, "Version does not contain a patch <major>.<patch> is expected. Got %s".formatted(version));
        Assertions.assertEquals(2, split.length, "Unexpected format detected. <major>.<patch> is expected. Got %s".formatted(version));
    }

    public static void all() throws IOException {
        assertVersionExists();
        assertVersionNotBlank();
        assertFormat();
    }
}
