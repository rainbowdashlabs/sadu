/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil.databases;

import de.chojo.sqlutil.jdbc.JdbcConfig;

/**
 * Represents a default database
 * @param <T> database type defined by the {@link SqlType}
 */
public abstract class DefaultType<T extends JdbcConfig<?>> implements SqlType<T> {

    @Override
    public String getVersion(String table) {
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
