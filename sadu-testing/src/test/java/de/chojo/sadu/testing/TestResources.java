/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.testing;

import java.nio.file.Path;

/**
 * The resources directory of the tests of this module, which holds the database files the checks run against.
 */
final class TestResources {
    static final Path PATH = Path.of("src/test/resources");

    private TestResources() {}
}
