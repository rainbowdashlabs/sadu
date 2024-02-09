/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.datasource.stage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.CheckReturnValue;

import javax.sql.DataSource;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

/**
 * Configuration stage, which is used to configurate a {@link com.zaxxer.hikari.HikariConfig}.
 */
public interface ConfigurationStage {
    /**
     * Set the maximum number of milliseconds that a client will wait for a connection from the pool. If this time is
     * exceeded without a connection becoming available, a SQLException will be thrown from {@link javax.sql.DataSource#getConnection()}.
     *
     * @param connectionTimeoutMs the connection timeout in milliseconds
     * @return Configuration Stage with value set.
     */
    @CheckReturnValue
    ConfigurationStage withConnectionTimeout(long connectionTimeoutMs);

    /**
     * This property controls the maximum amount of time (in milliseconds) that a connection is allowed to sit idle in
     * the pool. Whether a connection is retired as idle or not is subject to a maximum variation of +30 seconds, and
     * average variation of +15 seconds. A connection will never be retired as idle before this timeout. A value of 0
     * means that idle connections are never removed from the pool.
     *
     * @param idleTimeoutMs the idle timeout in milliseconds
     * @return Configuration Stage with value set.
     */
    @CheckReturnValue
    ConfigurationStage withIdleTimeout(long idleTimeoutMs);

    /**
     * This property controls the maximum lifetime of a connection in the pool. When a connection reaches this timeout,
     * even if recently used, it will be retired from the pool.
     * An in-use connection will never be retired, only when it is idle will it be removed.
     *
     * @param maxLifetimeMs the maximum connection lifetime in milliseconds
     * @return Configuration Stage with value set.
     */
    @CheckReturnValue
    ConfigurationStage withMaxLifetime(long maxLifetimeMs);

    /**
     * The property controls the maximum size that the pool is allowed to reach, including both idle and in-use connections.
     * Basically this value will determine the maximum number of actual connections to the database backend.
     * <p>
     * When the pool reaches this size, and no idle connections are available, calls to getConnection() will block for
     * up to connectionTimeout milliseconds before timing out.
     *
     * @param maxPoolSize the maximum number of connections in the pool
     * @return Configuration Stage with value set.
     */
    @CheckReturnValue
    ConfigurationStage withMaximumPoolSize(int maxPoolSize);

    /**
     * The property controls the minimum number of idle connections that HikariCP tries to maintain in the pool,
     * including both idle and in-use connections. If the idle connections dip below this value, HikariCP will make the
     * best effort to restore them quickly and efficiently.
     *
     * @param minIdle the minimum number of idle connections in the pool to maintain
     * @return Configuration Stage with value set.
     */
    @CheckReturnValue
    ConfigurationStage withMinimumIdle(int minIdle);

    /**
     * Set the default password to use for DataSource.getConnection(username, password) calls.
     *
     * @param password the password
     * @return Configuration Stage with value set.
     */
    @CheckReturnValue
    ConfigurationStage usingPassword(String password);

    /**
     * Set the default username used for DataSource.getConnection(username, password) calls.
     *
     * @param username the username
     * @return Configuration Stage with value set.
     */
    @CheckReturnValue
    ConfigurationStage usingUsername(String username);

    /**
     * Set the fully qualified class name of the JDBC {@link DataSource} that will be used create Connections.
     *
     * @param className the fully qualified name of the JDBC {@link DataSource} class
     * @return Configuration Stage with value set.
     */
    @CheckReturnValue
    ConfigurationStage withDataSourceClassName(Class<? extends DataSource> className);

    /**
     * Set the default auto-commit behavior of connections in the pool.
     *
     * @param isAutoCommit the desired auto-commit default for connections
     * @return Configuration Stage with value set.
     */
    @CheckReturnValue
    ConfigurationStage withAutoCommit(boolean isAutoCommit);

    /**
     * This property controls the keepalive interval for a connection in the pool. An in-use connection will never be
     * tested by the keepalive thread, only when it is idle will it be tested.
     *
     * @param keepaliveTimeMs the interval in which connections will be tested for aliveness, thus keeping them alive by the act of checking. Value is in milliseconds, default is 0 (disabled).
     * @return Configuration Stage with value set.
     */
    @CheckReturnValue
    ConfigurationStage withKeepaliveTime(long keepaliveTimeMs);

    /**
     * Set the name of the connection pool.  This is primarily used for the MBean
     * to uniquely identify the pool configuration.
     *
     * @param poolName the name of the connection pool to use
     * @return Configuration Stage with value set.
     */
    @CheckReturnValue
    ConfigurationStage withPoolName(String poolName);

    /**
     * Set the ScheduledExecutorService used for housekeeping.
     *
     * @param executor the ScheduledExecutorService
     * @return Configuration Stage with value set.
     */
    @CheckReturnValue
    ConfigurationStage withScheduledExecutor(ScheduledExecutorService executor);

    /**
     * Set the default schema name to be set on connections.
     *
     * @param schema the name of the default schema
     * @return Configuration Stage with value set.
     */
    @CheckReturnValue
    ConfigurationStage forSchema(String schema);

    /**
     * Set the thread factory to be used to create threads.
     *
     * @param threadFactory the thread factory (setting to null causes the default thread factory to be used)
     * @return Configuration Stage with value set.
     */
    @CheckReturnValue
    ConfigurationStage withThreadFactory(ThreadFactory threadFactory);

    /**
     * Allows the direct mutation of the HikariConfig, should be used for config options, that are not represented by a delegate.
     *
     * @param configConsumer the config consumer
     * @return Configuration Stage with value set
     */
    @CheckReturnValue
    ConfigurationStage withHikariConfig(Consumer<HikariConfig> configConsumer);

    /**
     * Create a new hikari data source
     *
     * @return hikari data source instance
     */
    @CheckReturnValue
    HikariDataSource build();
}
