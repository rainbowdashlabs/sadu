/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.updater;

import de.chojo.sadu.base.QueryFactory;
import de.chojo.sadu.databases.Database;
import de.chojo.sadu.jdbc.JdbcConfig;
import de.chojo.sadu.wrapper.QueryBuilderConfig;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckReturnValue;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
 * Every Major version directory has to contain a {@code setup.sql} file. This file represents the initial state of the database version.
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
 * <p>
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
 * During the update process the updater will look for a version table defined via {@link BaseSqlUpdaterBuilder#setVersionTable(String)}.
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
public class SqlUpdater<T extends JdbcConfig<?>, U extends BaseSqlUpdaterBuilder<T, ?>> extends QueryFactory {
    private static final Logger log = LoggerFactory.getLogger(SqlUpdater.class);
    private final SqlVersion version;
    private final Map<SqlVersion, Consumer<Connection>> preUpdateHook;
    private final Map<SqlVersion, Consumer<Connection>> postUpdateHook;
    private final DataSource source;
    private final String versionTable;
    private final QueryReplacement[] replacements;
    private final Database<T, U> type;

    protected SqlUpdater(DataSource source, QueryBuilderConfig config, String versionTable, QueryReplacement[] replacements, SqlVersion version, Database<T, U> type, Map<SqlVersion, Consumer<Connection>> preUpdateHook, Map<SqlVersion, Consumer<Connection>> postUpdateHook) {
        super(source, config);
        this.source = source;
        this.versionTable = versionTable;
        this.replacements = replacements;
        this.type = type;
        this.version = version;
        this.preUpdateHook = preUpdateHook;
        this.postUpdateHook = postUpdateHook;
    }

    /**
     * Creates a new {@link BaseSqlUpdaterBuilder} with a version set to a string located in {@code resources/database/version}.
     * <p>
     * This string requires the format {@code Major.Patch}. Patches are not supported.
     *
     * @param dataSource the data source to connect to the database
     * @param type       the sql type of the database
     * @param <T>        type of the database defined by the {@link Database}
     * @return new builder instance
     * @throws IOException if the version file does not exist.
     */
    @CheckReturnValue
    public static <T extends JdbcConfig<?>, U extends UpdaterBuilder<T, ?>> U builder(DataSource dataSource, Database<T, U> type) throws IOException {
        var version = "";
        try (var versionFile = SqlUpdater.class.getClassLoader().getResourceAsStream("database/version")) {
            version = new String(versionFile.readAllBytes(), StandardCharsets.UTF_8).trim();
        }

        var ver = version.split("\\.");
        return builder(dataSource, new SqlVersion(Integer.parseInt(ver[0]), Integer.parseInt(ver[1])), type);
    }

    /**
     * Creates a new {@link BaseSqlUpdaterBuilder} with a version set to a string located in {@code resources/database/version}.
     *
     * @param dataSource the data source to connect to the database
     * @param version    the version with {@code Major.Patch}
     * @param type       the sql type of the database
     * @param <T>        type of the database defined by the {@link Database}
     * @return builder instance
     */
    public static <T extends JdbcConfig<?>, U extends UpdaterBuilder<T, ?>> U builder(DataSource dataSource, SqlVersion version, Database<T, U> type) {
        var builder = type.newSqlUpdaterBuilder();
        builder.setSource(dataSource);
        builder.setVersion(version);
        return (U) builder;
    }

    @ApiStatus.Internal
    public void init() throws IOException, SQLException {
        forceDatabaseConsistency();

        var sqlVersion = getSqlVersion();

        if (sqlVersion.major() == version.major() && sqlVersion.patch() == version.patch()) {
            log.info(String.format("Database is up to date. No update is required! Version %s Patch %s",
                    sqlVersion.major(), sqlVersion.patch()));
            return;
        }

        if (sqlVersion.isNewer(version)) {
            throw new UpdateException("Database version is ahead. Newest know version is " + version + " but got " + sqlVersion + ".");
        }

        var patches = getPatchesFrom(sqlVersion.major(), sqlVersion.patch());

        log.info(String.format("Database is %s versions behind.", patches.size()));

        log.info("Performing update.");

        for (var patch : patches) {
            try {
                performUpdate(patch);
            } catch (SQLException e) {
                throw new UpdateException("Database update failed while applying patch %s!".formatted(patch.version()), e);
            }
        }
        log.info("Database update was successful!");
    }

    private void performUpdate(Patch patch) throws SQLException {
        log.info("Applying patch " + patch.version());
        try (var conn = source.getConnection()) {
            conn.setAutoCommit(false);
            var hook = preUpdateHook.get(patch.version());
            if (hook != null) {
                log.info("Running pre update hook");
                hook.accept(conn);
                log.info("Pre update hook applied");
            }

            for (var query : type.splitStatements(patch.query())) {
                try (var statement = conn.prepareStatement(adjust(query))) {
                    statement.execute();
                } catch (SQLException e) {
                    log.warn("Failed to execute statement:\n{}", query, e);
                    throw e;
                }
            }

            hook = postUpdateHook.get(patch.version());
            if (hook != null) {
                log.info("Running post update hook");
                hook.accept(conn);
                log.info("Post update hook applied");
            }
            conn.commit();
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

    protected void forceDatabaseConsistency() throws IOException, SQLException {
        try (var conn = source.getConnection()) {
            var isSetup = false;
            try (var stmt = conn.prepareStatement(type.createVersionTableQuery(versionTable))) {
                stmt.execute();
            }

            try (var stmt = conn.prepareStatement(type.versionQuery(versionTable))) {
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

    private SqlVersion getSqlVersion() {
        try (var conn = source.getConnection(); var statement = conn
                .prepareStatement(type.versionQuery(versionTable))) {
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new SqlVersion(resultSet.getInt("major"), resultSet.getInt("patch"));
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
        return getClass().getClassLoader()
                         .getResource("database/" + type.name() + "/" + major + "/patch_" + patch + ".sql") != null;
    }

    private String loadPatch(int major, int patch) throws IOException {
        return loadFromResource(major, "patch_" + patch + ".sql");
    }

    private String loadFromResource(Object... path) throws IOException {
        var patch = Arrays.stream(path).map(Object::toString).collect(Collectors.joining("/"));
        try (var patchFile = getClass().getClassLoader().getResourceAsStream("database/" + type.name() + "/" + patch)) {
            log.info("Loading resource {}", "database/" + type.name() + "/" + patch);
            return new String(patchFile.readAllBytes(), StandardCharsets.UTF_8);
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

    protected Database<T, U> type() {
        return type;
    }

}
