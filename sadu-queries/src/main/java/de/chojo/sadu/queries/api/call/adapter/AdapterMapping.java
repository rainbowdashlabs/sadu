/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.call.adapter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The AdapterMapping interface represents a functional interface used to map Java objects to a specific SQL data type
 * and perform the necessary conversions when binding the objects to a PreparedStatement.
 *
 * @param <T> the type of object to be adapted
 */
@FunctionalInterface
public interface AdapterMapping<T> {
    /**
     * Applies the given value to a PreparedStatement at the specified index using the provided mapping.
     *
     * @param stmt  the PreparedStatement to apply the value to
     * @param index the index at which to apply the value
     * @param value the value to be applied
     * @throws SQLException if a database access error occurs
     */
    void apply(PreparedStatement stmt, int index, T value) throws SQLException;
}
