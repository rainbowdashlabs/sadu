/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.testing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

class DatabaseVersionChecksTest {
    @Test
    void assertVersionExists() {
        Assertions.assertDoesNotThrow(() -> DatabaseVersionChecks.assertVersionExists());
    }

    @Test
    void assertVersionNotBlank() {
        Assertions.assertDoesNotThrow(() -> DatabaseVersionChecks.assertVersionNotBlank());
    }

    @Test
    void assertFormat() {
        Assertions.assertDoesNotThrow(() -> DatabaseVersionChecks.assertFormat());
        Assertions.assertThrows(AssertionFailedError.class, () -> DatabaseVersionChecks.assertFormat("1"));
        Assertions.assertThrows(AssertionFailedError.class, () -> DatabaseVersionChecks.assertFormat("1.0.0"));
    }
}
