/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.update;

import de.chojo.sadu.databases.Database;
import de.chojo.sadu.jdbc.PostgreSqlJdbc;
import de.chojo.sadu.updater.BaseSqlUpdaterBuilder;
import de.chojo.sadu.updater.SqlVersion;

import javax.annotation.CheckReturnValue;
import java.io.IOException;
import java.sql.SQLException;

public class PostgreSqlUpdaterBuilder extends BaseSqlUpdaterBuilder<PostgreSqlJdbc, PostgreSqlUpdaterBuilder> {

    private String[] schemas;

    public PostgreSqlUpdaterBuilder(Database<PostgreSqlJdbc, PostgreSqlUpdaterBuilder> type) {
        super(type);
    }

    /**
     * Set the schemas which should be created if they do not exist.
     *
     * @param schemas schemas
     * @return builder instance
     */
    @CheckReturnValue
    public PostgreSqlUpdaterBuilder setSchemas(String... schemas) {
        if (!type.hasSchemas()) {
            throw new IllegalStateException("This sql type does not support schemas");
        }
        this.schemas = schemas;
        return this;
    }

    @Override
    public void execute() throws SQLException, IOException {
        if (version == null) version = SqlVersion.load(classLoader);
        var updater = new PostgreSqlUpdater(source, versionTable, replacements, version, type, schemas, preUpdateHook, postUpdateHook, classLoader);
        updater.init();
    }
}
