/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.call.adapter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Adapter<T> {
    AdapterMapping<T> mapping();

    int type();

    default void apply(PreparedStatement stmt, int index, T object) throws SQLException {
        mapping().apply(stmt, index, object);
    }

    static <T> Adapter<T> create(Class<T> clazz, AdapterMapping<T> mapping, int type) {
        return new Adapter<>() {
            @Override
            public AdapterMapping<T> mapping() {
                return mapping;
            }

            @Override
            public int type() {
                return type;
            }
        };
    }
}
