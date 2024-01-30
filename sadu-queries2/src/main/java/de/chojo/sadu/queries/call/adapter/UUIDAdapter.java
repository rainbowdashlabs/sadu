/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.call.adapter;

import de.chojo.sadu.conversion.UUIDConverter;
import de.chojo.sadu.queries.api.call.adapter.Adapter;

import java.sql.Types;
import java.util.UUID;

/**
 * The UUIDAdapter class provides two Adapter instances for converting UUID objects to different types when binding them to a PreparedStatement.
 */
public final class UUIDAdapter {
    /**
     * Represents an Adapter for converting a UUID object to bytes and binding it to a PreparedStatement.
     * The AdapterMapping implementation used is {@link UUIDConverter#convert(UUID)}.
     * The SQL data type used is {@link Types#BINARY}.
     */
    public static final Adapter<UUID> AS_BYTES = Adapter.create(UUID.class, (stmt, index, value) -> stmt.setBytes(index, UUIDConverter.convert(value)), Types.BINARY);
    /**
     * Represents an Adapter for converting a UUID object to a String and binding it to a PreparedStatement.
     */
    public static final Adapter<UUID> AS_STRING = Adapter.create(UUID.class, (stmt, index, value) -> stmt.setString(index, value.toString()), Types.VARCHAR);

    private UUIDAdapter() {
        throw new UnsupportedOperationException("This is a utility class.");
    }
}
