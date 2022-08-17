/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.exceptions;

import java.sql.SQLException;

/**
 * Utilitites for database handling
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
