package de.chojo.sqlutil.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.chojo.sqlutil.datasource.stage.ConfigurationStage;
import de.chojo.sqlutil.datasource.stage.PropertyStage;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * Class to create a {@link HikariDataSource} with a builder pattern.
 */
public class DataSourceCreator implements PropertyStage, ConfigurationStage {
    private final Properties properties = new Properties();
    private HikariConfig hikariConfig;

    private DataSourceCreator() {
    }

    /**
     * Create a new DataSource creator.
     *
     * @param dataSourceClass data source implementation
     * @return a {@link DataSourceCreator} in {@link PropertyStage}.
     */
    public static PropertyStage create(Class<? extends DataSource> dataSourceClass) {
        var dataSourceCreator = new DataSourceCreator();
        dataSourceCreator.withProperty("dataSourceClassName", dataSourceClass.getName());
        return dataSourceCreator;
    }

    /**
     * Create a new DataSource creator.
     *
     * @param dataSourceClass data source implementation
     * @return a {@link DataSourceCreator} in {@link PropertyStage}.
     *
     * @deprecated Use {@link DataSourceCreator#create(Class)} instead. It is
     */
    @Deprecated
    public static PropertyStage create(String dataSourceClass) {
        var dataSourceCreator = new DataSourceCreator();
        dataSourceCreator.withProperty("dataSourceClassName", dataSourceClass);
        return dataSourceCreator;
    }

    @Override
    public PropertyStage withProperty(String key, String value) {
        properties.setProperty(key, value);
        return this;
    }

    @Override
    public PropertyStage withAddress(String address) {
        properties.setProperty("dataSource.serverName", address);
        return this;
    }

    @Override
    public PropertyStage withPort(String portNumber) {
        properties.setProperty("dataSource.portNumber", portNumber);
        return this;
    }

    @Override
    public PropertyStage withPort(int portNumber) {
        properties.setProperty("dataSource.portNumber", String.valueOf(portNumber));
        return this;
    }

    @Override
    public PropertyStage withUser(String user) {
        properties.setProperty("dataSource.serverName", user);
        return this;
    }

    @Override
    public PropertyStage withPassword(String password) {
        properties.setProperty("dataSource.password", password);
        return this;
    }

    @Override
    public PropertyStage forDatabase(String database) {
        properties.setProperty("dataSource.databaseName", database);
        return this;
    }

    @Override
    public PropertyStage withSettings(DbConfig config) {
        config.apply(properties);
        return this;
    }

    @Override
    public ConfigurationStage create() {
        hikariConfig = new HikariConfig(properties);
        return this;
    }

    @Override
    public ConfigurationStage setConnectionTimeout(long connectionTimeoutMs) {
        hikariConfig.setConnectionTimeout(connectionTimeoutMs);
        return this;
    }

    @Override
    public ConfigurationStage setIdleTimeout(long idleTimeoutMs) {
        hikariConfig.setIdleTimeout(idleTimeoutMs);
        return this;
    }

    @Override
    public ConfigurationStage setMaxLifetime(long maxLifetimeMs) {
        hikariConfig.setMaxLifetime(maxLifetimeMs);
        return this;
    }

    @Override
    public ConfigurationStage setMaximumPoolSize(int maxPoolSize) {
        hikariConfig.setMaximumPoolSize(maxPoolSize);
        return this;
    }

    @Override
    public ConfigurationStage setMinimumIdle(int minIdle) {
        hikariConfig.setMinimumIdle(minIdle);
        return this;
    }

    @Override
    public ConfigurationStage setPassword(String password) {
        hikariConfig.setPassword(password);
        return this;
    }

    @Override
    public ConfigurationStage setUsername(String username) {
        hikariConfig.setUsername(username);
        return this;
    }

    @Override
    public ConfigurationStage setAutoCommit(boolean isAutoCommit) {
        hikariConfig.setAutoCommit(isAutoCommit);
        return this;
    }

    @Override
    public ConfigurationStage setKeepaliveTime(long keepaliveTimeMs) {
        hikariConfig.setKeepaliveTime(keepaliveTimeMs);
        return this;
    }

    @Override
    public ConfigurationStage setPoolName(String poolName) {
        hikariConfig.setPoolName(poolName);
        return this;
    }

    @Override
    public ConfigurationStage setScheduledExecutor(ScheduledExecutorService executor) {
        hikariConfig.setScheduledExecutor(executor);
        return this;
    }

    @Override
    public ConfigurationStage setSchema(String schema) {
        hikariConfig.setSchema(schema);
        return this;
    }

    @Override
    public ConfigurationStage setThreadFactory(ThreadFactory threadFactory) {
        hikariConfig.setThreadFactory(threadFactory);
        return this;
    }

    @Override
    public HikariDataSource build() {
        return new HikariDataSource(hikariConfig);
    }
}
