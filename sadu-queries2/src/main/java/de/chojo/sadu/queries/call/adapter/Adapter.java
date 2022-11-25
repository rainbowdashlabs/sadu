/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.call.adapter;

import de.chojo.sadu.conversion.UUIDConverter;
import de.chojo.sadu.exceptions.ThrowingBiConsumer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

public interface Adapter {
    static Adapter asString(UUID uuid) {
        return create(uuid, (stmt, index) -> stmt.setString(index, uuid.toString()), Types.VARCHAR);
    }

    static Adapter asBytes(UUID uuid) {
        return create(uuid, (stmt, index) -> stmt.setBytes(index, UUIDConverter.convert(uuid)), Types.BIT);
    }

    private static <T> Adapter create(T object, ThrowingBiConsumer<PreparedStatement, Integer, SQLException> apply, int type) {
        return new Adapter() {
            @Override
            public ThrowingBiConsumer<PreparedStatement, Integer, SQLException> apply() {
                return apply;
            }

            @Override
            public int type() {
                return type;
            }

            @Override
            public Object object() {
                return object;
            }
        };

    }

    ThrowingBiConsumer<PreparedStatement, Integer, SQLException> apply();

    int type();

    Object object();
}
