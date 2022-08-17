/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.datasource;

import org.jetbrains.annotations.Nullable;

import java.util.Properties;

/**
 * Class which provides basic data for a database
 */
public class DbConfig {
    private final String address;
    private final String port;
    private final String user;
    private final String password;
    private final String database;

    /**
     * Create a new DbConfiguration.
     * <p>
     * Values which are null will be ignored
     *
     * @param address  address
     * @param port     port
     * @param user     user
     * @param password password
     * @param database database
     */
    public DbConfig(@Nullable String address, @Nullable String port, @Nullable String user, @Nullable String password, @Nullable String database) {
        this.address = address;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
    }

    /**
     * Apply the settings to a property
     *
     * @param properties properties to apply the settings on
     */
    public void apply(Properties properties) {
        if (address != null) properties.setProperty("dataSource.serverName", address);
        if (port != null) properties.setProperty("dataSource.portNumber", port);
        if (user != null) properties.setProperty("dataSource.user", user);
        if (password != null) properties.setProperty("dataSource.password", password);
        if (database != null) properties.setProperty("dataSource.databaseName", database);
    }
}
