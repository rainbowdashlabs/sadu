/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.core.databases;

import de.chojo.sadu.core.jdbc.JdbcConfig;
import de.chojo.sadu.core.updater.UpdaterBuilder;

/**
 * Represents a default database
 *
 * @param <T> database type defined by the {@link Database}
 */
public interface DefaultDatabase<T extends JdbcConfig<?>, U extends UpdaterBuilder<T, ?>> extends Database<T, U> {

    @Override
    default String versionQuery(String table) {
        return String.format("SELECT major, patch FROM %s LIMIT 1", table);
    }

    @Override
    default String insertVersion(String table) {
        return String.format("INSERT INTO %s VALUES (?, ?)", table);
    }

    @Override
    default String deleteVersion(String table) {
        return String.format("DELETE FROM %s;", table);
    }

    @Override
    default String createVersionTableQuery(String table) {
        return String.format("CREATE TABLE IF NOT EXISTS %s(major INTEGER, patch INTEGER);", table);
    }
}
