/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.chojo.sadu.databases.SqlType;
import de.chojo.sadu.datasource.stage.ConfigurationStage;
import de.chojo.sadu.datasource.stage.JdbcStage;
import de.chojo.sadu.jdbc.JdbcConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Class to create a {@link HikariDataSource} with a builder pattern.
 *
 * @param <T> database type defined by the {@link SqlType}
 */
public class DataSourceCreator<T extends JdbcConfig<?>> implements JdbcStage<T>, ConfigurationStage {
    private static final Logger log = LoggerFactory.getLogger(DataSourceCreator.class);
    private final T builder;
    private HikariConfig hikariConfig;

    private DataSourceCreator(SqlType<T> type) {
        this.builder = type.jdbcBuilder();
    }

    /**
     * Create a new DataSource creator.
     *
     * @param type The type of database which is targeted by this data source
     * @param <T>  database type defined by the {@link SqlType}
     * @return a {@link DataSourceCreator} in {@link JdbcStage}.
     */
    public static <T extends JdbcConfig<?>> JdbcStage<T> create(SqlType<T> type) {
        return new DataSourceCreator<>(type);
    }

    @Override
    public JdbcStage<T> configure(Consumer<T> builder) {
        builder.accept(this.builder);
        return this;
    }

    @Override
    public ConfigurationStage create() {
        loadDriverClass();
        var jdbcUrl = builder.jdbcUrl();
        log.info("Creating Hikari config using jdbc url: {}", jdbcUrl.replaceAll("password=.+?(&|$)", "password=******"));
        hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrl);
        return this;
    }

    private void loadDriverClass() {
        try {
            Class.forName(builder.driverClass());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not load driver class. Is class in class path? Use #setDriverClass when using relocation.", e);
        }
    }

    @Override
    public ConfigurationStage withConnectionTimeout(long connectionTimeoutMs) {
        hikariConfig.setConnectionTimeout(connectionTimeoutMs);
        return this;
    }

    @Override
    public ConfigurationStage withIdleTimeout(long idleTimeoutMs) {
        hikariConfig.setIdleTimeout(idleTimeoutMs);
        return this;
    }

    @Override
    public ConfigurationStage withMaxLifetime(long maxLifetimeMs) {
        hikariConfig.setMaxLifetime(maxLifetimeMs);
        return this;
    }

    @Override
    public ConfigurationStage withMaximumPoolSize(int maxPoolSize) {
        hikariConfig.setMaximumPoolSize(maxPoolSize);
        return this;
    }

    @Override
    public ConfigurationStage withMinimumIdle(int minIdle) {
        hikariConfig.setMinimumIdle(minIdle);
        return this;
    }

    @Override
    public ConfigurationStage usingPassword(String password) {
        hikariConfig.setPassword(password);
        return this;
    }

    @Override
    public ConfigurationStage usingUsername(String username) {
        hikariConfig.setUsername(username);
        return this;
    }

    @Override
    public DataSourceCreator<T> withDataSourceClassName(Class<? extends DataSource> className) {
        hikariConfig.setDataSourceClassName(className.getName());
        return this;
    }

    @Override
    public ConfigurationStage withAutoCommit(boolean isAutoCommit) {
        hikariConfig.setAutoCommit(isAutoCommit);
        return this;
    }

    @Override
    public ConfigurationStage withKeepaliveTime(long keepaliveTimeMs) {
        hikariConfig.setKeepaliveTime(keepaliveTimeMs);
        return this;
    }

    @Override
    public ConfigurationStage withPoolName(String poolName) {
        hikariConfig.setPoolName(poolName);
        return this;
    }

    @Override
    public ConfigurationStage withScheduledExecutor(ScheduledExecutorService executor) {
        hikariConfig.setScheduledExecutor(executor);
        return this;
    }

    @Override
    public ConfigurationStage forSchema(String schema) {
        hikariConfig.setSchema(schema);
        return this;
    }

    @Override
    public ConfigurationStage withThreadFactory(ThreadFactory threadFactory) {
        hikariConfig.setThreadFactory(threadFactory);
        return this;
    }

    @Override
    public HikariDataSource build() {
        return new HikariDataSource(hikariConfig);
    }
}
