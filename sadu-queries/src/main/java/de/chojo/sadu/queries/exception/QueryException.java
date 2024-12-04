package de.chojo.sadu.queries.exception;

import de.chojo.sadu.queries.query.ParsedQueryImpl;

import java.io.Serial;
import java.sql.SQLException;

public class QueryException extends SQLException {

    @Serial
    private static final long serialVersionUID = 1;

    /**
     * Cause of the exception
     */
    private final SQLException cause;

    /**
     * Creates a new exception.
     */
    public QueryException(ParsedQueryImpl query, SQLException throwable) {
        super("An error occurred: %s\nWhile executing query:\n%s".formatted(throwable.getMessage(), query.sql().sql().stripIndent()), throwable);
        cause = throwable;
    }

    /**
     * SQL state of the exception
     *
     * @return sql state
     */
    public String getSQLState() {
        return cause.getSQLState();
    }

    /**
     * Error code of the exception
     *
     * @return error code
     */
    public int getErrorCode() {
        return cause.getErrorCode();
    }
}
