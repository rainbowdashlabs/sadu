/*
 *     SPDX-License-Identifier: LGPL-3.0-only
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.conversion;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UUIDConverterTest {

    @Test
    void convert() {
        // Check if you can still convert with bytes
        UUID uuid = UUID.fromString("7ccf6e1c-68fc-442d-88d0-341b315a29cd");
        byte[] uuidAsByte = UUIDConverter.convert(uuid);

        assertEquals(UUIDConverter.convert(uuidAsByte), uuid);

        // Check if the new null check works
        assertNull(UUIDConverter.convert((byte[]) null));
    }
}
