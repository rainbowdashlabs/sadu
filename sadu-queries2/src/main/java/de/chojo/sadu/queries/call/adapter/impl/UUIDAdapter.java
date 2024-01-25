/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.call.adapter.impl;

import de.chojo.sadu.conversion.UUIDConverter;
import de.chojo.sadu.queries.call.adapter.Adapter;

import java.sql.Types;
import java.util.UUID;

public class UUIDAdapter {
    public static final Adapter<UUID> AS_BYTES = Adapter.create(UUID.class, (stmt, index, value) -> stmt.setBytes(index, UUIDConverter.convert(value)), Types.BINARY);
    public static final Adapter<UUID> AS_STRING = Adapter.create(UUID.class, (stmt, index, value) -> stmt.setString(index, value.toString()), Types.VARCHAR);
}
