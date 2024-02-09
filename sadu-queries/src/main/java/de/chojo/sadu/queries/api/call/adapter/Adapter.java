/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.call.adapter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Function;

/**
 * The Adapter interface provides a way to map Java objects to a specific SQL data type and perform the necessary conversions when binding the objects to a PreparedStatement.
 *
 * @param <T> the type of object to be adapted
 */
public interface Adapter<T> {
    /**
     * Creates a new Adapter instance for the given class, mapping, and type.
     *
     * @param <T>     the type of object to be adapted
     * @param clazz   the class of the object to be adapted
     * @param mapping the AdapterMapping implementation that performs the necessary conversions for binding the object to a PreparedStatement
     * @param type    the SQL type of the Adapter as defined by java.sql.Types
     * @return a new Adapter instance
     */
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

    /**
     * Creates a new Adapter instance for the given class, mapping, and type.
     *
     * @param <T>     the type of object to be adapted
     * @param mapping the AdapterMapping implementation that performs the necessary conversions for binding the object to a PreparedStatement
     * @param type    the SQL type of the Adapter as defined by java.sql.Types
     * @return a new Adapter instance
     */
    static <T, V> Adapter<T> create(AdapterMapping<V> mapping, int type, Function<T, V> map) {
        return new Adapter<>() {
            @Override
            public AdapterMapping<T> mapping() {
                return (stmt, index, value) -> mapping.apply(stmt, index, map.apply(value));
            }

            @Override
            public int type() {
                return type;
            }
        };
    }

    /**
     * Creates a new Adapter instance for the given class, mapping, and type.
     *
     * @param <T>     the type of object to be adapted
     * @param mapping the AdapterMapping implementation that performs the necessary conversions for binding the object to a PreparedStatement
     * @param type    the SQL type of the Adapter as defined by java.sql.Types
     * @return a new Adapter instance
     */
    static <T> Adapter<T> create(AdapterMapping<T> mapping, int type) {
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

    /**
     * Returns the AdapterMapping for the given Adapter instance.
     *
     * @return the AdapterMapping for the Adapter instance.
     */
    AdapterMapping<T> mapping();

    /**
     * Returns the sql type of the Adapter as defined by {@link java.sql.Types}
     *
     * @return the type of the Adapter.
     */
    int type();

    /**
     * Applies the given object to a PreparedStatement at the specified index using the provided mapping.
     *
     * @param stmt   the PreparedStatement to apply the object to
     * @param index  the index at which to apply the object
     * @param object the object to be applied
     * @throws SQLException if a database access error occurs
     */
    default void apply(PreparedStatement stmt, int index, T object) throws SQLException {
        mapping().apply(stmt, index, object);
    }
}
