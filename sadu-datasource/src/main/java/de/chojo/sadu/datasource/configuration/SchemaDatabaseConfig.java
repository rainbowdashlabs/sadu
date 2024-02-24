/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.datasource.configuration;

import de.chojo.sadu.core.configuration.DatabaseConfig;
import de.chojo.sadu.core.configuration.SchemaProvider;

/**
 * Record which provides basic data for a database connection with a schema
 */

public record SchemaDatabaseConfig(String host, String port, String user, String password, String database,
                                   String schema) implements DatabaseConfig, SchemaProvider {
}
