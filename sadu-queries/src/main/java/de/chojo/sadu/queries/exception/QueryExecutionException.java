/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.exception;

import de.chojo.sadu.queries.query.QueryImpl;

import java.io.Serial;
import java.sql.SQLException;

/**
 * Exception to wrap {@link Exception} as {@link Exception} thrown during queries executed by {@link QueryImpl}
 */
public class QueryExecutionException extends SQLException {
    @Serial
    private static final long serialVersionUID = 1;
    /**
     * Cause of the exception
     */
    private SQLException cause;

    /**
     * Creates a new exception
     *
     * @param message message
     */
    public QueryExecutionException(String message) {
        super(message);
    }

    /**
     * @throws ClassCastException If the throwable is not a {@link SQLException}
     */
    @Override
    public synchronized Throwable initCause(Throwable cause) {
        this.cause = (SQLException) cause;
        return super.initCause(cause);
    }

    @Override
    public String getSQLState() {
        return cause.getSQLState();
    }

    @Override
    public int getErrorCode() {
        return cause.getErrorCode();
    }

    @Override
    public String getMessage() {
        return cause.getMessage();
    }
}
