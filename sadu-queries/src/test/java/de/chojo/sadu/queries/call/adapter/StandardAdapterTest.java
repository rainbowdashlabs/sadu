/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.call.adapter;

import org.junit.jupiter.api.Test;

import java.sql.Types;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardAdapterTest {
    @Test
    void shortIsSmallint() {
        assertEquals(Types.SMALLINT, StandardAdapter.SHORT.type());
    }

    @Test
    void byteIsTinyint() {
        assertEquals(Types.TINYINT, StandardAdapter.BYTE.type());
    }
}
