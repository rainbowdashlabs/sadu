/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.sqlite.databases;

import de.chojo.sadu.core.databases.DefaultDatabase;
import de.chojo.sadu.core.updater.StatementSplitter;
import de.chojo.sadu.core.updater.UpdaterBuilder;
import de.chojo.sadu.sqlite.jdbc.SqLiteJdbc;
import de.chojo.sadu.updater.BaseSqlUpdaterBuilder;

/**
 * Represents a SqLite database.
 */
public class SqLite extends DefaultDatabase<SqLiteJdbc, BaseSqlUpdaterBuilder<SqLiteJdbc, ?>> {

    private static final SqLite type = new SqLite();

    private SqLite() {
    }

    /**
     * The SqLite type.
     *
     * @return database type
     */
    public static SqLite sqlite() {
        return type;
    }

    /**
     * The SqLite type.
     *
     * @return database type
     */
    public static SqLite get() {
        return type;
    }

    @Override
    public String createVersionTableQuery(String table) {
        return String.format("CREATE TABLE IF NOT EXISTS %s(major INT, patch INT);", table);
    }

    @Override
    public String name() {
        return "sqlite";
    }

    @Override
    public SqLiteJdbc jdbcBuilder() {
        return new SqLiteJdbc();
    }

    @Override
    public String[] splitStatements(String queries) {
        return StatementSplitter.split(queries);
    }

    @Override
    public UpdaterBuilder<SqLiteJdbc, BaseSqlUpdaterBuilder<SqLiteJdbc, ?>> newSqlUpdaterBuilder() {
        return new BaseSqlUpdaterBuilder<>(this);
    }
}
