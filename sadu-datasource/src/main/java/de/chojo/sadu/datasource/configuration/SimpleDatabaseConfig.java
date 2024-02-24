/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.datasource.configuration;

/**
 * Record which provides basic data for a database connection
 */
public record SimpleDatabaseConfig(String host, String port, String user, String password,
                                   String database) implements de.chojo.sadu.core.configuration.DatabaseConfig {
}
