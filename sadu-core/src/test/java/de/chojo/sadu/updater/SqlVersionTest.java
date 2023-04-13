/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.updater;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SqlVersionTest {
    SqlVersion version_1_5 = new SqlVersion(1, 5);
    SqlVersion version_1_6 = new SqlVersion(1, 6);
    SqlVersion version_2_5 = new SqlVersion(2, 5);

    @Test
    void isHigher() {
        Assertions.assertFalse(version_1_5.isNewer(version_1_6));
        Assertions.assertFalse(version_1_6.isNewer(version_2_5));
        Assertions.assertTrue(version_1_6.isNewer(version_1_5));
        Assertions.assertTrue(version_2_5.isNewer(version_1_6));
    }

    @Test
    void isLower() {
        Assertions.assertTrue(version_1_5.isOlder(version_1_6));
        Assertions.assertTrue(version_1_6.isOlder(version_2_5));
        Assertions.assertFalse(version_1_6.isOlder(version_1_5));
        Assertions.assertFalse(version_2_5.isOlder(version_1_6));
    }
}
