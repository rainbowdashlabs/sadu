/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.testing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

class DatabaseChecksTest {

    @Test
    void assertExistingDatabase() {
        DatabaseChecks.assertDatabase("postgresql");
    }

    @Test
    void assertInvalidgDatabase() {
        Assertions.assertThrows(AssertionFailedError.class, () -> DatabaseChecks.assertDatabase("invalidDb"));
    }
}
