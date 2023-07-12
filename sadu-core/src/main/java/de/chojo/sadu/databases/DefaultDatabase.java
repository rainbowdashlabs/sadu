/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.databases;

import de.chojo.sadu.updater.UpdaterBuilder;
import de.chojo.sadu.jdbc.JdbcConfig;

/**
 * Represents a default database
 *
 * @param <T> database type defined by the {@link Database}
 */
public abstract class DefaultDatabase<T extends JdbcConfig<?>, U extends UpdaterBuilder<T, ?>> implements Database<T, U> {

    @Override
    public String versionQuery(String table) {
        return String.format("SELECT major, patch FROM %s LIMIT 1", table);
    }

    @Override
    public String insertVersion(String table) {
        return String.format("INSERT INTO %s VALUES (?, ?)", table);
    }

    @Override
    public String deleteVersion(String table) {
        return String.format("DELETE FROM %s;", table);
    }

    @Override
    public String createVersionTableQuery(String table) {
        return String.format("CREATE TABLE IF NOT EXISTS %s(major INTEGER, patch INTEGER);", table);
    }
}
