/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil.updater;

import de.chojo.sqlutil.base.QueryFactoryHolder;
import de.chojo.sqlutil.databases.SqlType;
import de.chojo.sqlutil.jdbc.JdbcConfig;
import de.chojo.sqlutil.logging.LoggerAdapter;
import de.chojo.sqlutil.wrapper.QueryBuilderConfig;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * An SQL updater which performs database updates via upgrade scripts.
 * <p>
 * A version is defined via version identifier {@code Major.Patch}.
 * <p>
 * There are three types of scripts:
 * <p>
 *
 * <b>Setup</b>
 * <p>
 * Every Major version directory has to contain a {@code setup.sql} file. This file represents the inital state of the database version.
 * <p>
 * During the initial setup the {@code setup.sql} script of the highest major version is executed.
 * <p>
 *
 * <b>Patch</b>
 * <p>
 * Every patch version requires a {@code patch_x.sql} file where {@code x} is the number of the patch version.
 * <p>
 * These files get applied one after another until the current version is reached.
 * <p>
 *
 * <b>Migrate</b>
 * <p>
 * Every Major version which has a following major version requires a {@code migrate.sql} script.
 * <p>
 * This script should update the database to the same state as a clean installation via the new {@code setup.sql} would do.
 *
 *
 * <pre>{@code
 * database
 * ├── postgres
 * │   ├── 1                # 1.x
 * │   │   ├── setup.sql    # 1.0
 * │   │   ├── patch_1.sql  # 1.1
 * │   │   ├── patch_2.sql  # 1.2
 * │   │   ├── patch_3.sql  # 1.3
 * │   │   └── migrate.sql  # 1.3 -> 2.0
 * │   └── 2
 * │       ├── setup.sql    # 2.0
 * │       └── patch_1.sql  # 2.1
 * └── mysql
 *     ├── 1
 *     │   ├── setup.sql
 *     │   ├── patch_1.sql
 *     │   ├── patch_2.sql
 *     │   ├── patch_3.sql
 *     │   └── migrate.sql
 *     └── 2
 *         ├── setup.sql
 *         └── patch_1.sql
 *       }</pre>
 *
 *
 * <b>Setup Process</b>
 * <p>
 * During the update process the updater will look for a version table defined via {@link SqlUpdaterBuilder#setVersionTable(String)}.
 * <p>
 * If this table is not present it will be created and the highest setup script will be executed and all existing patches.
 *
 * <p>
 * <b>Update Process</b>
 * <p>
 * If the version table is present the updater will read the version and execute all following patches including the migration scripts.
 * <p>
 * If the current version would be 1.2, but the target version would be 2.1 the following script would be executed:
 *
 * <pre>
 *      - 1/patch_3.sql
 *      - 1/migrate.sql
 *      - 2/patch_1.sql
 *  </pre>
 */
public class SqlUpdater<T extends JdbcConfig<?>> extends QueryFactoryHolder {
    private final SqlVersion version;
    private String[] schemas;
    private static final Logger log = getLogger(SqlUpdater.class);
    private final DataSource source;
    private final String versionTable;
    private final QueryReplacement[] replacements;
    private final SqlType<T> type;

    private SqlUpdater(DataSource source, QueryBuilderConfig config, String versionTable, QueryReplacement[] replacements, SqlVersion version, SqlType<T> type, String[] schemas) {
        super(source, config);
        this.source = source;
        this.versionTable = versionTable;
        this.replacements = replacements;
        this.type = type;
        this.version = version;
        this.schemas = schemas;
    }

    /**
     * Creates a new {@link SqlUpdaterBuilder} with a version set to a string located in {@code resources/database/version}.
     * <p>
     * This string requires the format {@code Major.Patch}. Patches are not supported.
     *
     * @param dataSource the data source to connect to the database
     * @param type       the sql type of the database
     * @param <T>        type of the database defined by the {@link SqlType}
     * @return new builder instance
     * @throws IOException if the version file does not exist.
     */
    public static <T extends JdbcConfig<?>> SqlUpdaterBuilder builder(DataSource dataSource, SqlType<T> type) throws IOException {
        var version = "";
        try (var versionFile = SqlUpdater.class.getClassLoader().getResourceAsStream("database/version")) {
            version = new String(versionFile.readAllBytes()).trim();
        }

        var ver = version.split("\\.");
        return new SqlUpdaterBuilder(dataSource, new SqlVersion(Integer.parseInt(ver[0]), Integer.parseInt(ver[1])), type);
    }

    /**
     * Creates a new {@link SqlUpdaterBuilder} with a version set to a string located in {@code resources/database/version}.
     *
     * @param dataSource the data source to connect to the database
     * @param version    the version with {@code Major.Patch}
     * @param type       the sql type of the database
     * @param <T>        type of the database defined by the {@link SqlType}
     * @return builder instance
     */
    public static <T extends JdbcConfig<?>> SqlUpdaterBuilder<T> builder(DataSource dataSource, SqlVersion version, SqlType<T> type) {
        return new SqlUpdaterBuilder<>(dataSource, version, type);
    }

    private void init() throws IOException, SQLException {
        forceDatabaseConsistency();

        var versionInfo = getVersionInfo();

        if (versionInfo.version() == version.major() && versionInfo.patch() == version.patch()) {
            log.info(String.format("Database is up to date. No update is required! Version %s Patch %s",
                    versionInfo.version(), versionInfo.patch()));
            return;
        }

        var patches = getPatchesFrom(versionInfo.version(), versionInfo.patch());

        log.info(String.format("Database is %s versions behind.", patches.size()));

        log.info("Performing update.");

        for (var patch : patches) {
            try {
                performUpdate(patch);
            } catch (SQLException e) {
                throw new UpdateException("Database update failed!", e);
            }
        }
        log.info("Database update was successful!");
    }

    private void performUpdate(Patch patch) throws SQLException {
        log.info("Applying patch.");
        try (var conn = source.getConnection()) {
            for (var query : type.splitStatements(patch.query())) {
                try (var statement = conn.prepareStatement(adjust(query))) {
                    statement.execute();
                }
            }
        } catch (SQLException e) {
            log.warn("Database update failed", e);
            throw e;
        }
        log.info("Patch applied.");
        updateVersion(patch.major(), patch.patch());
        if (patch.patch() != 0) {
            log.info(String.format("Deployed patch %s.%s to database.", patch.major(), patch.patch()));
        } else {
            log.info(String.format("Migrated database to version %s.", patch.major()));
        }
    }

    private void forceDatabaseConsistency() throws IOException, SQLException {
        try (var conn = source.getConnection()) {
            if (type.hasSchemas()) {

                for (var schema : schemas) {
                    if (!schemaExists(schema)) {
                        try (var stmt = conn.prepareStatement(type.createSchema(schema))) {
                            stmt.execute();
                            log.info("Schema {} did not exist. Created.", schema);
                        }
                    } else {
                        log.info("Schema {} does exist. Proceeding.", schema);
                    }
                }
            }

            var isSetup = false;
            try (var stmt = conn.prepareStatement(type.createVersionTableQuery(versionTable))) {
                stmt.execute();
            }

            try (var stmt = conn.prepareStatement(type.getVersion(versionTable))) {
                var resultSet = stmt.executeQuery();
                if (!resultSet.next()) {
                    log.info("Version table " + versionTable + " is empty. Attempting database setup.");
                    isSetup = true;
                }
            }

            if (isSetup) {
                log.info(String.format("Setup database with version %s", version.major()));
                for (var query : type.splitStatements(getSetup())) {
                    try (var stmt = conn.prepareStatement(adjust(query))) {
                        stmt.execute();
                    }
                }
                log.info("Initial setup complete. Ready to patch.");
                updateVersion(version.major(), 0);
            }
        }
    }

    private boolean schemaExists(String schema) {
        try (var conn = source.getConnection(); var stmt = conn.prepareStatement(type.schemaExists())) {
            stmt.setString(1, schema);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            log.error("Could not check if schema {} exists", schema, e);
        }
        return false;
    }

    /**
     * Update the current database Version.
     *
     * @param version new version of database
     * @param patch   new patch of database
     */
    private void updateVersion(int version, int patch) {
        try (var conn = source.getConnection()) {
            try (var statement = conn.prepareStatement(type.deleteVersion(versionTable))) {
                statement.execute();
            }
            try (var statement = conn.prepareStatement(type.insertVersion(versionTable))) {
                statement.setInt(1, version);
                statement.setInt(2, patch);
                statement.execute();
            } catch (SQLException e) {
                log.error("Failed change database version!", e);
                throw new UpdateException("Failed change database version", e);
            }
            log.info(String.format("Set database to version %s patch %s!", version, patch));
        } catch (SQLException e) {
            log.error("Failed change database version!", e);
            throw new UpdateException("Failed change database version", e);
        }
    }

    private VersionInfo getVersionInfo() {
        try (var conn = source.getConnection(); var statement = conn
                .prepareStatement(type.getVersion(versionTable))) {
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new VersionInfo(resultSet.getInt("major"), resultSet.getInt("patch"));
            }
            throw new UpdateException("Could not retrieve database version!");
        } catch (SQLException e) {
            log.error("Could not check if schema exists in database!", e);
            throw new UpdateException("Could not retrieve database version!", e);
        }
    }

    private List<Patch> getPatchesFrom(int major, int patch) throws IOException {
        List<Patch> patches = new ArrayList<>();
        var currPatch = patch;
        for (var currMajor = major; currMajor <= version.major(); currMajor++) {
            while (currPatch < version.patch()) {
                currPatch++;
                if (patchExists(currMajor, currPatch)) {
                    patches.add(new Patch(major, currPatch, loadPatch(currMajor, currPatch)));
                } else if (currMajor != version.major()) {
                    patches.add(new Patch(major + 1, 0, getMigrationFromVersion(major)));
                    currPatch = 0;
                    break;
                }
            }
        }
        return patches;
    }

    private boolean patchExists(int major, int patch) {
        return getClass().getClassLoader().getResource("database/" + type.getName() + "/" + major + "/patch_" + patch + ".sql") != null;
    }

    private String loadPatch(int major, int patch) throws IOException {
        return loadFromResource(major, "patch_" + patch + ".sql");
    }

    private String loadFromResource(Object... path) throws IOException {
        var patch = Arrays.stream(path).map(Object::toString).collect(Collectors.joining("/"));
        try (var patchFile = getClass().getClassLoader().getResourceAsStream("database/" + type.getName() + "/" + patch)) {
            return new String(patchFile.readAllBytes());
        }
    }

    private String getMigrationFromVersion(int major) throws IOException {
        return loadFromResource(major - 1, "migration.sql");
    }

    private String getSetup() throws IOException {
        return loadFromResource(version.major(), "setup.sql");
    }

    private String adjust(String query) {
        var result = query;
        for (var replacement : replacements) {
            result = replacement.apply(result);
        }
        return result;
    }

    /**
     * Class to build a {@link SqlUpdater} with a builder pattern
     *
     * @param <T> The type of the jdbc link defined by the {@link SqlType}
     */
    public static class SqlUpdaterBuilder<T extends JdbcConfig<?>> {
        private final DataSource source;
        private final SqlVersion version;
        private final SqlType<T> type;
        private String versionTable = "version";
        private QueryReplacement[] replacements = new QueryReplacement[0];
        private LoggerAdapter logger;
        private QueryBuilderConfig config = QueryBuilderConfig.builder().throwExceptions().build();
        private String[] schemas;

        private SqlUpdaterBuilder(DataSource dataSource, SqlVersion version, SqlType<T> type) {
            this.source = dataSource;
            this.version = version;
            this.type = type;
        }

        /**
         * Set the schemas which should be created if they do not exist.
         *
         * @param schemas schemas
         * @return builder instance
         */
        public SqlUpdaterBuilder<T> setSchemas(String... schemas) {
            if (!type.hasSchemas()) {
                throw new IllegalStateException("This sql type does not support schemas");
            }
            this.schemas = schemas;
            return this;
        }


        /**
         * The version table which contains major and patch version.
         * <p>
         * Changing this later might cause a loss of data.
         *
         * @param versionTable name of the version table
         * @return builder instance
         */
        public SqlUpdaterBuilder<T> setVersionTable(String versionTable) {
            this.versionTable = versionTable;
            return this;
        }

        /**
         * Replacements which should be applied to the executed scripts. Can be used to change schema names
         * or other variables during deployment
         *
         * @param replacements replacements
         * @return builder instance
         */
        public SqlUpdaterBuilder<T> setReplacements(QueryReplacement... replacements) {
            this.replacements = replacements;
            return this;
        }

        /**
         * Set a logger adapter used for logging.
         *
         * @param logger logger adapter
         * @return builder instance
         */
        public SqlUpdaterBuilder<T> withLogger(LoggerAdapter logger) {
            this.logger = logger;
            return this;
        }

        /**
         * Set the {@link QueryBuilderConfig} for the underlying {@link QueryFactoryHolder}
         *
         * @param config config so apply
         * @return builder instance
         */
        public SqlUpdaterBuilder<T> withConfig(QueryBuilderConfig config) {
            this.config = config;
            return this;
        }

        /**
         * Build the updater and start the update process.
         *
         * @throws SQLException If execution of the scripts fails
         * @throws IOException  If the scripts can't be read.
         */
        public void execute() throws SQLException, IOException {
            var sqlUpdater = new SqlUpdater<>(source, config, versionTable, replacements, version, type, schemas);
            sqlUpdater.init();
        }
    }
}
