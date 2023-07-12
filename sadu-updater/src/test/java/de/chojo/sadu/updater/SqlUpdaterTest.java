/*
 *     SPDX-License-Identifier: LGPL-3.0-only
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.updater;

import de.chojo.sadu.databases.PostgreSql;

import javax.sql.DataSource;

import java.io.IOException;
import java.sql.SQLException;

class SqlUpdaterTest {
    DataSource dataSource;
    public void update() throws IOException, SQLException {
        SqlUpdater.builder(dataSource, PostgreSql.get())
                .setReplacements(new QueryReplacement("dev_schema", "live_schema"))
                .setVersionTable("my_app_version")
                .setSchemas("live_schema")
                .execute();
    }

}
