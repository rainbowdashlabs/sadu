/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.conversion;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Convert uuids to bytes and vice versa
 */
public final class UUIDConverter {
    private UUIDConverter() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    /**
     * Convert an uuid to a byte array
     *
     * @param uuid uuid
     * @return uuid as byte array
     */
    public static byte[] convert(UUID uuid) {
        return ByteBuffer.wrap(new byte[16])
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits())
                .array();
    }

    /**
     * Convert a byte array to an uuid.
     *
     * @param bytes bytes
     * @return new uuid instance
     */
    public static UUID convert(byte[] bytes) {
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }
}
