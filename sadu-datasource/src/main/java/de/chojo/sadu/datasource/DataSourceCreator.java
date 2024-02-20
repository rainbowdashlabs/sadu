/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.chojo.sadu.core.databases.Database;
import de.chojo.sadu.core.jdbc.JdbcConfig;
import de.chojo.sadu.core.jdbc.RemoteJdbcConfig;
import de.chojo.sadu.datasource.exceptions.DriverClassNotFoundException;
import de.chojo.sadu.datasource.stage.ConfigurationStage;
import de.chojo.sadu.datasource.stage.JdbcStage;
import org.jetbrains.annotations.CheckReturnValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

/**
 * Class to create a {@link HikariDataSource} with a builder pattern.
 *
 * @param <T> database type defined by the {@link Database}
 */
public class DataSourceCreator<T extends JdbcConfig<?>> implements JdbcStage<T>, ConfigurationStage {
    private static final Logger log = LoggerFactory.getLogger(DataSourceCreator.class);
    private final T builder;
    private HikariConfig hikariConfig;

    private DataSourceCreator(Database<T, ?> type) {
        builder = type.jdbcBuilder();
    }

    /**
     * Create a new DataSource creator.
     *
     * @param type The type of database which is targeted by this data source
     * @param <T>  database type defined by the {@link Database}
     * @return a DataSourceCreator in {@link JdbcStage}.
     */
    @CheckReturnValue
    public static <T extends JdbcConfig<?>> JdbcStage<T> create(Database<T, ?> type) {
        return new DataSourceCreator<>(type);
    }

    @Override
    @CheckReturnValue
    public JdbcStage<T> configure(Consumer<T> builder) {
        builder.accept(this.builder);
        return this;
    }

    @Override
    @CheckReturnValue
    public ConfigurationStage create() {
        loadDriverClass();
        RemoteJdbcConfig.Credentials credentials = RemoteJdbcConfig.Credentials.EMPTY;
        if (builder instanceof RemoteJdbcConfig) {
            credentials = ((RemoteJdbcConfig<?>) builder).userCredentials();
        }
        var jdbcUrl = builder.jdbcUrl();
        log.info("Creating Hikari config using jdbc url: {}", jdbcUrl.replaceAll("password=.+?(&|$)", "password=******"));
        hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrl);
        credentials.user().ifPresent(u -> hikariConfig.setUsername(u.valueRaw()));
        credentials.password().ifPresent(u -> hikariConfig.setPassword(u.valueRaw()));
        return this;
    }

    private void loadDriverClass() {
        try {
            Class.forName(builder.driverClass());
        } catch (ClassNotFoundException e) {
            throw new DriverClassNotFoundException("Could not load driver class. Is class in class path? Use #setDriverClass when using relocation.", e);
        }
    }

    @Override
    @CheckReturnValue
    public ConfigurationStage withConnectionTimeout(long connectionTimeoutMs) {
        hikariConfig.setConnectionTimeout(connectionTimeoutMs);
        return this;
    }

    @Override
    @CheckReturnValue
    public ConfigurationStage withIdleTimeout(long idleTimeoutMs) {
        hikariConfig.setIdleTimeout(idleTimeoutMs);
        return this;
    }

    @Override
    @CheckReturnValue
    public ConfigurationStage withMaxLifetime(long maxLifetimeMs) {
        hikariConfig.setMaxLifetime(maxLifetimeMs);
        return this;
    }

    @Override
    @CheckReturnValue
    public ConfigurationStage withMaximumPoolSize(int maxPoolSize) {
        hikariConfig.setMaximumPoolSize(maxPoolSize);
        return this;
    }

    @Override
    @CheckReturnValue
    public ConfigurationStage withMinimumIdle(int minIdle) {
        hikariConfig.setMinimumIdle(minIdle);
        return this;
    }

    @Override
    @CheckReturnValue
    public ConfigurationStage usingPassword(String password) {
        hikariConfig.setPassword(password);
        return this;
    }

    @Override
    @CheckReturnValue
    public ConfigurationStage usingUsername(String username) {
        hikariConfig.setUsername(username);
        return this;
    }

    @Override
    @CheckReturnValue
    public DataSourceCreator<T> withDataSourceClassName(Class<? extends DataSource> className) {
        hikariConfig.setDataSourceClassName(className.getName());
        return this;
    }

    @Override
    @CheckReturnValue
    public ConfigurationStage withAutoCommit(boolean isAutoCommit) {
        hikariConfig.setAutoCommit(isAutoCommit);
        return this;
    }

    @Override
    @CheckReturnValue
    public ConfigurationStage withKeepaliveTime(long keepaliveTimeMs) {
        hikariConfig.setKeepaliveTime(keepaliveTimeMs);
        return this;
    }

    @Override
    @CheckReturnValue
    public ConfigurationStage withPoolName(String poolName) {
        hikariConfig.setPoolName(poolName);
        return this;
    }

    @Override
    @CheckReturnValue
    public ConfigurationStage withScheduledExecutor(ScheduledExecutorService executor) {
        hikariConfig.setScheduledExecutor(executor);
        return this;
    }

    @Override
    @CheckReturnValue
    public ConfigurationStage forSchema(String schema) {
        hikariConfig.setSchema(schema);
        return this;
    }

    @Override
    @CheckReturnValue
    public ConfigurationStage withThreadFactory(ThreadFactory threadFactory) {
        hikariConfig.setThreadFactory(threadFactory);
        return this;
    }

    @Override
    @CheckReturnValue
    public ConfigurationStage withHikariConfig(Consumer<HikariConfig> configConsumer) {
        configConsumer.accept(hikariConfig);
        return this;
    }

    @Override
    @CheckReturnValue
    public ConfigurationStage withHikariConfig(HikariConfig config) {
        hikariConfig = config;
        return this;
    }

    @Override
    @CheckReturnValue
    public HikariDataSource build() {
        return new HikariDataSource(hikariConfig);
    }
}
