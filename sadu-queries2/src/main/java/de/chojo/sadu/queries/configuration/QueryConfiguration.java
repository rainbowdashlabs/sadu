package de.chojo.sadu.queries.configuration;

import de.chojo.sadu.mapper.RowMapperRegistry;
import de.chojo.sadu.queries.exception.WrappedQueryExecutionException;
import de.chojo.sadu.queries.stages.ParsedQuery;
import de.chojo.sadu.queries.stages.Query;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class QueryConfiguration implements AutoCloseable {
    protected static final AtomicReference<QueryConfiguration> DEFAULT = new AtomicReference<>(null);
    protected final Query query;
    protected final DataSource dataSource;
    protected final boolean atomic;
    protected final boolean throwExceptions;
    protected final Consumer<SQLException> exceptionHandler;
    protected final RowMapperRegistry rowMapperRegistry;

    QueryConfiguration(@Nullable DataSource dataSource, boolean atomic, boolean throwExceptions, Consumer<SQLException> exceptionHandler, RowMapperRegistry rowMapperRegistry) {
        query = null;
        this.dataSource = dataSource;
        this.atomic = atomic;
        this.throwExceptions = throwExceptions;
        this.exceptionHandler = exceptionHandler;
        this.rowMapperRegistry = rowMapperRegistry;
    }

    QueryConfiguration(Query query, DataSource dataSource, boolean atomic, boolean throwExceptions, Consumer<SQLException> exceptionHandler, RowMapperRegistry rowMapperRegistry) {
        this.query = query;
        this.dataSource = dataSource;
        this.atomic = atomic;
        this.throwExceptions = throwExceptions;
        this.exceptionHandler = exceptionHandler;
        this.rowMapperRegistry = rowMapperRegistry;
    }

    public QueryConfiguration forQuery(Query query) {
        return new QueryConfiguration(query, dataSource, atomic, throwExceptions, exceptionHandler, rowMapperRegistry);
    }

    public QueryConfiguration withAtomicConnection() {
        return new ConnectedQueryConfiguration(query, dataSource, true, throwExceptions, exceptionHandler, rowMapperRegistry);
    }

    @Override
    public void close() {
        try {
            if (connection() != null && !connection().isClosed()) {
                if (query.exceptions().isEmpty()) {
                    if (atomic()) {
                        // TODO commit if atomic
                        connection().commit();
                    }
                }
                if (!connection().isClosed()) {
                    connection().close();
                }
            }
        } catch (SQLException e) {
            handleException(e);
        }
    }

    public void handleException(SQLException e) {
        exceptionHandler.accept(e);
        query.logException(e);
        if (throwExceptions) {
            throw (WrappedQueryExecutionException) new WrappedQueryExecutionException(e.getMessage()).initCause(e);
        }
    }

    public boolean atomic() {
        return atomic;
    }

    public boolean throwExceptions() {
        return throwExceptions;
    }

    public RowMapperRegistry rowMapperRegistry() {
        return rowMapperRegistry;
    }

    public DataSource dataSource() {
        return dataSource;
    }

    public Connection connection() {
        return null;
    }

    public boolean hasConnection() {
        return false;
    }

    public ParsedQuery query(@Language("sql") String sql, Object... format) {
        return Query.query(this, sql, format);
    }
}