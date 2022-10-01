/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.updater;

import de.chojo.sadu.databases.PostgreSql;

import javax.sql.DataSource;

import java.io.IOException;
import java.sql.SQLException;

class SqlUpdaterTest {
    DataSource dataSource;
    public void update() throws IOException, SQLException {
        SqlUpdaterBuilder.builder(dataSource, PostgreSql.get())
                .setReplacements(new QueryReplacement("dev_schema", "live_schema"))
                .setVersionTable("my_app_version")
                .setSchemas("live_schema")
                .execute();
    }

}
