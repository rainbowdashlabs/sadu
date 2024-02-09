/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.testing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.io.IOException;

class PatchChecksTest {

    @Test
    void checkFiles() throws IOException {
        Assertions.assertDoesNotThrow(() -> PatchChecks.checkFiles(1, "2.1", "postgresql"));
        Assertions.assertThrows(AssertionFailedError.class,() -> PatchChecks.checkFiles(1, "2.2", "postgresql"));
    }
}
