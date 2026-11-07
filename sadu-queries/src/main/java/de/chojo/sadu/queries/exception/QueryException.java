/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.exception;

import de.chojo.sadu.mapper.exceptions.MappingException;
import de.chojo.sadu.queries.query.ParsedQueryImpl;
import de.chojo.sadu.queries.query.TokenizedQuery;

import java.io.Serial;
import java.sql.SQLException;

/**
 * An exception raised while executing a query, carrying the SQL of the query that failed.
 * <p>
 * Mapping failures are wrapped as well, so they reach the configured exception handler like any other query error.
 * The original exception is available via {@link #getCause()}.
 */
public class QueryException extends SQLException {

    @Serial
    private static final long serialVersionUID = 1;

    /**
     * Creates a new exception for a database error.
     */
    public QueryException(ParsedQueryImpl query, SQLException throwable) {
        super(message(query.sql(), throwable), throwable.getSQLState(), throwable.getErrorCode(), throwable);
    }

    /**
     * Creates a new exception for a result that could not be mapped.
     */
    public QueryException(TokenizedQuery sql, MappingException throwable) {
        super(message(sql, throwable), throwable);
    }

    private static String message(TokenizedQuery sql, Exception throwable) {
        return "An error occurred: %s\nWhile executing query:\n%s"
                .formatted(throwable.getMessage(), sql.sql().stripIndent());
    }
}
