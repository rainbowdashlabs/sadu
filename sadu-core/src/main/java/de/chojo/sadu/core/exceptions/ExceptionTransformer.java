/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.core.exceptions;

import java.sql.SQLException;

/**
 * Utilities for database exception handling.
 */
public final class ExceptionTransformer {
    private ExceptionTransformer() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    /**
     * Transforms a SQLException in an easy readable string with a message.
     *
     * @param ex sql exception
     * @return input as pretty string
     */
    public static String prettyException(SQLException ex) {
        return String.format("SQLException: %s%nSQLState: %s%nVendorError: %s",
                ex.getMessage(), ex.getSQLState(), ex.getErrorCode());
    }

    /**
     * Transforms a SQLException in an easy readable string with a message.
     *
     * @param message Message
     * @param ex      sql exception
     * @return input as pretty string
     */
    public static String prettyException(String message, SQLException ex) {
        return String.format("%s:%n%s",
                message, prettyException(ex));
    }
}
