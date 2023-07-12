/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.update;

import de.chojo.sadu.databases.Database;
import de.chojo.sadu.jdbc.PostgreSqlJdbc;
import de.chojo.sadu.updater.SqlUpdater;
import de.chojo.sadu.updater.QueryReplacement;
import de.chojo.sadu.updater.SqlVersion;
import de.chojo.sadu.wrapper.QueryBuilderConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.Consumer;

public class PostgreSqlUpdater extends SqlUpdater<PostgreSqlJdbc, PostgreSqlUpdaterBuilder> {

    private static final Logger log = LoggerFactory.getLogger(PostgreSqlUpdater.class);

    private final String[] schemas;

    protected PostgreSqlUpdater(DataSource source, QueryBuilderConfig config, String versionTable, QueryReplacement[] replacements, SqlVersion version, Database<PostgreSqlJdbc, PostgreSqlUpdaterBuilder> type, String[] schemas, Map<SqlVersion, Consumer<Connection>> preUpdateHook, Map<SqlVersion, Consumer<Connection>> postUpdateHook) {
        super(source, config, versionTable, replacements, version, type, preUpdateHook, postUpdateHook);
        this.schemas = schemas;
    }

    @Override
    protected void forceDatabaseConsistency() throws IOException, SQLException {
        try (var conn = source().getConnection()) {
            if (type().hasSchemas()) {
                for (var schema : schemas) {
                    if (!schemaExists(schema)) {
                        try (var stmt = conn.prepareStatement(type().createSchema(schema))) {
                            stmt.execute();
                            log.info("Schema {} did not exist. Created.", schema);
                        }
                    } else {
                        log.info("Schema {} does exist. Proceeding.", schema);
                    }
                }
            }
        }

        super.forceDatabaseConsistency();
    }

    private boolean schemaExists(String schema) {
        try (var conn = source().getConnection(); var stmt = conn.prepareStatement(type().schemaExists())) {
            stmt.setString(1, schema);
            var row = stmt.executeQuery();
            if (row.next()) {
                return row.getBoolean(1);
            }
        } catch (SQLException e) {
            log.error("Could not check if schema {} exists", schema, e);
        }
        return false;
    }
}
