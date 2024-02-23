/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.testing;

import de.chojo.sadu.core.databases.Database;

import java.io.IOException;

public final class SaduTests {

    private SaduTests() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    /**
     * Executes all sadu related tests
     *
     * @param baseVersion the start version of the database
     * @param database    the databases to check
     * @throws IOException if any file could not be read
     */
    public static void execute(int baseVersion, Database<?, ?>... database) throws IOException {
        DatabaseChecks.assertDatabase(database);
        DatabaseVersionChecks.all();
        PatchChecks.checkFiles(baseVersion, database);
    }
}
