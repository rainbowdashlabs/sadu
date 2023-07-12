/*
 *     SPDX-License-Identifier: LGPL-3.0-only
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper;

import de.chojo.sadu.base.DataHolder;
import de.chojo.sadu.exceptions.ThrowingConsumer;
import de.chojo.sadu.exceptions.ThrowingFunction;
import de.chojo.sadu.mapper.MapperConfig;
import de.chojo.sadu.mapper.rowmapper.RowMapper;
import de.chojo.sadu.wrapper.exception.QueryExecutionException;
import de.chojo.sadu.wrapper.exception.WrappedQueryExecutionException;
import de.chojo.sadu.wrapper.stage.ConfigurationStage;
import de.chojo.sadu.wrapper.stage.InsertStage;
import de.chojo.sadu.wrapper.stage.QueryStage;
import de.chojo.sadu.wrapper.stage.ResultStage;
import de.chojo.sadu.wrapper.stage.RetrievalStage;
import de.chojo.sadu.wrapper.stage.StatementStage;
import de.chojo.sadu.wrapper.stage.UpdateStage;
import de.chojo.sadu.wrapper.util.ParamBuilder;
import de.chojo.sadu.wrapper.util.Row;
import de.chojo.sadu.wrapper.util.UpdateResult;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * This query builder can be used to execute one or more queries onto a database via a connection provided by a datasource.
 * <p>
 * The query builder is a stage organized object. Every call will be invoked on a stage of the same query builder.
 * <p>
 * You may execute as many updates as you want. You may only get the returned results of one query, which must be the last.
 * <p>
 * All methods which are not labeled with a {@code Sync} suffix will be executed in an async context. Async method will
 * provide a callback via a {@link CompletableFuture}.
 * <p>
 * All queries will be executed in an atomic transaction. This means that data will be only written if all queries are executed successfully.
 * This behaviour can be changed by calling {@link QueryBuilderConfig.Builder#notAtomic()} ()} on config creation.
 * <p>
 * Any exception thrown while executing queries will be wrapped into an {@link QueryExecutionException}. This exception
 * was created on query submission to the query builder. Note that this is not the execution, which may be called on
 * another thread. This exception will help you to trace back async calls.
 * <p>
 * Any {@link SQLException} thrown inside the query builder will not be thrown but logged by default.
 *
 * @param <T> type of query return type
 */
public class QueryBuilder<T> extends DataHolder implements ConfigurationStage<T>, QueryStage<T>, StatementStage<T>, ResultStage<T>, RetrievalStage<T>, InsertStage {
    private final Queue<QueryTask> tasks = new ArrayDeque<>();
    private final QueryExecutionException executionException;
    private final WrappedQueryExecutionException wrappedExecutionException;
    private final Class<T> clazz;
    private String currQuery;
    private ThrowingConsumer<PreparedStatement, SQLException> currStatementConsumer;
    private RowMapper<T> currRowMapper;
    private AtomicReference<QueryBuilderConfig> config;
    private MapperConfig mapperConfig = new MapperConfig();

    private QueryBuilder(DataSource dataSource, Class<T> clazz) {
        super(dataSource);
        this.clazz = clazz;
        executionException = new QueryExecutionException("An error occurred while executing a query.");
        wrappedExecutionException = new WrappedQueryExecutionException("An error occurred while executing a query.");
    }

    /**
     * Create a new query builder with a defined return type. Use it for selects.
     *
     * @param source datasource for connection
     * @param clazz  class of required return type. Doesn't matter if you want a list or single result.
     * @param <T>    type of return type
     * @return a new query builder in a {@link QueryStage}
     */
    public static <T> ConfigurationStage<T> builder(DataSource source, Class<T> clazz) {
        return new QueryBuilder<>(source, clazz);
    }

    /**
     * Create a new Query builder without a defined return type. Use it for updates.
     *
     * @param source datasource for connection
     * @return a new query builder in a {@link QueryStage}
     */
    public static ConfigurationStage<?> builder(DataSource source) {
        return new QueryBuilder<>(source, null);
    }

    // CONFIGURATION STAGE

    @Override
    public QueryStage<T> configure(AtomicReference<QueryBuilderConfig> config) {
        this.config = config;
        return this;
    }

    @Override
    public QueryStage<T> defaultConfig() {
        config = QueryBuilderConfig.defaultConfig();
        return this;
    }

    @Override
    public QueryStage<T> defaultConfig(Consumer<QueryBuilderConfig.Builder> builderModifier) {
        var builder = config.get().toBuilder();
        builderModifier.accept(builder);
        config = new AtomicReference<>(builder.build());
        return this;
    }

    // QUERY STAGE

    @Override
    public StatementStage<T> query(String query) {
        currQuery = query;
        return this;
    }

    @Override
    public ResultStage<T> queryWithoutParams(String query) {
        currQuery = query;
        return emptyParams();
    }

    // STATEMENT STAGE

    @Override
    public ResultStage<T> params(ThrowingConsumer<PreparedStatement, SQLException> stmt) {
        currStatementConsumer = stmt;
        return this;
    }

    @Override
    public ResultStage<T> parameter(ThrowingConsumer<ParamBuilder, SQLException> params) {
        currStatementConsumer = stmt -> params.accept(new ParamBuilder(stmt));
        return this;
    }

    // RESULT STAGE

    @Override
    public RetrievalStage<T> readRow(ThrowingFunction<T, Row, SQLException> mapper) {
        Objects.requireNonNull(clazz);
        currRowMapper = RowMapper.forClass(clazz).mapper(mapper).build();
        queueTask();
        return this;
    }

    @Override
    public RetrievalStage<T> map(MapperConfig mapperConfig) {
        Objects.requireNonNull(clazz);
        this.mapperConfig = mapperConfig.copy();
        queueTask();
        return this;
    }

    @Override
    public UpdateStage update() {
        currRowMapper = null;
        queueTask();
        return this;
    }

    @Override
    public InsertStage insert() {
        update();
        return this;
    }

    @Override
    public QueryStage<T> append() {
        currRowMapper = null;
        queueTask();
        return this;
    }

    private void queueTask() {
        tasks.add(new QueryTask(clazz, currQuery, currStatementConsumer, currRowMapper, mapperConfig));
    }

    // RETRIEVAL STAGE

    // LISTS  RETRIEVAL

    @Override
    public CompletableFuture<List<T>> all() {
        return CompletableFuture.supplyAsync(this::allSync);
    }

    @Override
    public CompletableFuture<List<T>> all(Executor executor) {
        return CompletableFuture.supplyAsync(this::allSync, executor);
    }

    @Override
    public List<T> allSync() {
        try (var conn = getConnection()) {
            autoCommit(conn);
            var results = executeAndGetLast(conn).retrieveResults(conn);
            commit(conn);
            return results;
        } catch (QueryExecutionException e) {
            logDbError(e);
        } catch (SQLException e) {
            handleException(e);
        }
        return Collections.emptyList();
    }

    @Override
    public CompletableFuture<List<Long>> keys() {
        return CompletableFuture.supplyAsync(this::keysSync);
    }

    @Override
    public CompletableFuture<List<Long>> keys(Executor executor) {
        return CompletableFuture.supplyAsync(this::keysSync, executor);
    }

    @Override
    public List<Long> keysSync() {
        try (var conn = getConnection()) {
            autoCommit(conn);
            var results = executeAndGetLast(conn).retrieveKeys(conn);
            commit(conn);
            return results;
        } catch (QueryExecutionException e) {
            logDbError(e);
        } catch (SQLException e) {
            handleException(e);
        }
        return Collections.emptyList();
    }

    @Override
    public void logDbError(SQLException e) {
        config.get().exceptionHandler().ifPresentOrElse(consumer -> consumer.accept(e), () -> super.logDbError(e));
    }

    // SINGLE RETRIEVAL

    @Override
    public CompletableFuture<Optional<T>> first() {
        return CompletableFuture.supplyAsync(this::firstSync);
    }

    @Override
    public CompletableFuture<Optional<T>> first(Executor executor) {
        return CompletableFuture.supplyAsync(this::firstSync, executor);
    }

    @Override
    public Optional<T> firstSync() {
        try (var conn = getConnection()) {
            autoCommit(conn);
            var result = executeAndGetLast(conn).retrieveResult(conn);
            commit(conn);
            return result;
        } catch (SQLException e) {
            handleException(e);
        }
        return Optional.empty();
    }

    @Override
    public CompletableFuture<Optional<Long>> key() {
        return CompletableFuture.supplyAsync(this::keySync);
    }

    @Override
    public CompletableFuture<Optional<Long>> key(Executor executor) {
        return CompletableFuture.supplyAsync(this::keySync, executor);
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    @Override
    public Optional<Long> keySync() {
        try (var conn = getConnection()) {
            autoCommit(conn);
            var result = executeAndGetLast(conn).retrieveKey(conn);
            commit(conn);
            return result;
        } catch (SQLException e) {
            handleException(e);
        }
        return Optional.empty();
    }

    // UPDATE STAGE

    @Override
    public UpdateResult sendSync() {
        return new UpdateResult(executeSync());
    }

    @Override
    public CompletableFuture<UpdateResult> send() {
        return CompletableFuture.supplyAsync(() -> new UpdateResult(executeSync()));
    }

    @Override
    public CompletableFuture<UpdateResult> send(Executor executor) {
        return CompletableFuture.supplyAsync(() -> new UpdateResult(executeSync()), executor);
    }

    @Override
    public CompletableFuture<Integer> execute() {
        return CompletableFuture.supplyAsync(this::executeSync, config.get().executor());
    }

    @Override
    public CompletableFuture<Integer> execute(Executor executor) {
        return CompletableFuture.supplyAsync(this::executeSync, executor);
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    @Override
    public int executeSync() {
        try (var conn = getConnection()) {
            autoCommit(conn);
            var update = executeAndGetLast(conn).update(conn);
            commit(conn);
            return update;
        } catch (SQLException e) {
            handleException(e);
        }
        return 0;
    }


    /*
    Execute all queries, since we are only interested in the result of the last of our queries.
    That's why we will execute all queries inside this method regardless of the method which calls this method
    We will use a single connection for this, since the user may be interested in the last inserted id or something.
    */
    private QueryTask executeAndGetLast(Connection conn) throws QueryExecutionException {
        while (tasks.size() > 1) {
            tasks.poll().execute(conn);
        }
        return tasks.poll();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void handleException(SQLException e) {
        if (config.get().isThrowing()) {
            wrappedExecutionException.initCause(e);
            throw wrappedExecutionException;
        }
        executionException.initCause(e);
        config.get().exceptionHandler()
              .ifPresentOrElse(consumer -> consumer.accept(executionException), () -> logDbError(executionException));
    }

    private void autoCommit(Connection conn) throws SQLException {
        conn.setAutoCommit(!config.get().isAtomic());
    }

    private void commit(Connection conn) throws SQLException {
        if (config.get().isAtomic()) conn.commit();
    }

    @SuppressWarnings("JDBCPrepareStatementWithNonConstantString")
    private class QueryTask {
        private final Class<T> clazz;
        private final String query;
        private final ThrowingConsumer<PreparedStatement, SQLException> statementConsumer;
        private final MapperConfig mapperConfig;
        private final QueryExecutionException executionException;
        private RowMapper<T> rowMapper;

        private QueryTask(Class<T> clazz, String currQuery, ThrowingConsumer<PreparedStatement, SQLException> statementConsumer,
                          RowMapper<T> rowMapper, MapperConfig mapperConfig) {
            this.clazz = clazz;
            query = currQuery;
            this.statementConsumer = statementConsumer;
            this.rowMapper = rowMapper;
            this.mapperConfig = mapperConfig;
            executionException = new QueryExecutionException("An error occurred while executing a query.");
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        private void initAndThrow(SQLException e) throws QueryExecutionException {
            executionException.initCause(e);
            throw executionException;
        }

        public List<T> retrieveResults(Connection conn) throws QueryExecutionException {
            List<T> results = new ArrayList<>();
            try (var stmt = conn.prepareStatement(query)) {
                statementConsumer.accept(stmt);
                var resultSet = stmt.executeQuery();
                var row = new Row(resultSet, mapperConfig);
                while (resultSet.next()) results.add(mapper(resultSet).map(row));
            } catch (SQLException e) {
                initAndThrow(e);
            }
            return results;
        }

        public Optional<T> retrieveResult(Connection conn) throws QueryExecutionException {
            try (var stmt = conn.prepareStatement(query)) {
                statementConsumer.accept(stmt);
                var resultSet = stmt.executeQuery();
                var row = new Row(resultSet, mapperConfig);
                if (resultSet.next()) return Optional.ofNullable(mapper(resultSet).map(row));
            } catch (SQLException e) {
                initAndThrow(e);
            }
            return Optional.empty();
        }

        public void execute(Connection conn) throws QueryExecutionException {
            try (var stmt = conn.prepareStatement(query)) {
                statementConsumer.accept(stmt);
                stmt.execute();
            } catch (SQLException e) {
                initAndThrow(e);
            }
        }

        public List<Long> retrieveKeys(Connection conn) throws QueryExecutionException {
            List<Long> results = new ArrayList<>();
            try (var stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statementConsumer.accept(stmt);
                stmt.execute();
                var generatedKeys = stmt.getGeneratedKeys();
                while (generatedKeys.next()) results.add(generatedKeys.getLong(1));
            } catch (SQLException e) {
                initAndThrow(e);
            }
            return results;
        }

        public Optional<Long> retrieveKey(Connection conn) throws QueryExecutionException {
            try (var stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statementConsumer.accept(stmt);
                stmt.execute();
                var generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) return Optional.of(generatedKeys.getLong(1));
            } catch (SQLException e) {
                initAndThrow(e);
            }
            return Optional.empty();
        }

        public int update(Connection conn) throws QueryExecutionException {
            try (var stmt = conn.prepareStatement(query)) {
                statementConsumer.accept(stmt);
                return stmt.executeUpdate();
            } catch (SQLException e) {
                initAndThrow(e);
            }
            return 0;
        }

        private RowMapper<T> mapper(ResultSet resultSet) throws SQLException {
            if (rowMapper != null) {
                return rowMapper;
            }
            rowMapper = config.get().rowMappers().findOrWildcard(clazz, resultSet, mapperConfig);
            return rowMapper;
        }
    }
}
