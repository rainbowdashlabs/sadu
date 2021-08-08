package de.chojo.sqlutil.updater;

import de.chojo.sqlutil.updater.logging.LoggerAdapter;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class SqlUpdater {
    private final SqlVersion version;
    private final LoggerAdapter log;
    private final DataSource source;
    private final String versionTable;
    private final QueryReplacement[] replacements;
    private final SqlType type;

    private SqlUpdater(DataSource source, String versionTable, QueryReplacement[] replacements, SqlVersion version, LoggerAdapter loggerAdapter, SqlType type) {
        this.source = source;
        this.versionTable = versionTable;
        this.replacements = replacements;
        this.log = loggerAdapter;
        this.type = type;
        this.version = version;
    }

    public static SqlUpdaterBuilder builder(DataSource dataSource, SqlType type) throws IOException {
        var version = "";
        try (var in = SqlUpdater.class.getClassLoader().getResourceAsStream("database/version")) {
            version = new String(in.readAllBytes()).trim();
        }

        var ver = version.split("\\.");
        return new SqlUpdaterBuilder(dataSource, new SqlVersion(Integer.parseInt(ver[0]), Integer.parseInt(ver[1])), type);
    }

    public static SqlUpdaterBuilder builder(DataSource dataSource, SqlVersion version, SqlType type) throws IOException {
        return new SqlUpdaterBuilder(dataSource, version, type);
    }

    public void init() throws IOException, SQLException {
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
            var isSetup = false;
            try (var stmt = conn.prepareStatement(type.createVersionTableQuery(versionTable))) {
                stmt.execute();
            }

            try (var stmt = conn.prepareStatement(type.getVersion(versionTable))) {
                var rs = stmt.executeQuery();
                if (!rs.next()) {
                    log.info("Version table is empty. Attempting database setup.");
                    isSetup = true;
                }
            }

            if (!isSetup) {
                log.info(String.format("Setup database with version %s", version.major()));
                for (var query : type.splitStatements(getSetup())) {
                    try (var stmt = conn.prepareStatement(adjust(query))) {
                        stmt.execute();
                        log.info("Initial setup complete. Ready to patch.");
                    }
                }
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
        var p = Arrays.stream(path).map(Object::toString).collect(Collectors.joining("/"));
        try (var in = getClass().getClassLoader().getResourceAsStream("database/" + type.getName() + "/" + p)) {
            return new String(in.readAllBytes());
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

    public static class SqlUpdaterBuilder {
        private final DataSource source;
        private final SqlVersion version;
        private final SqlType type;
        private String versionTable = "version";
        private QueryReplacement[] replacements = new QueryReplacement[0];
        private LoggerAdapter logger;

        public SqlUpdaterBuilder(DataSource dataSource, SqlVersion version, SqlType type) {
            this.source = dataSource;
            this.version = version;
            this.type = type;
        }

        public SqlUpdaterBuilder setVersionTable(String versionTable) {
            this.versionTable = versionTable;
            return this;
        }

        public SqlUpdaterBuilder setReplacements(QueryReplacement... replacements) {
            this.replacements = replacements;
            return this;
        }

        public SqlUpdaterBuilder withLogger(LoggerAdapter logger) {
            this.logger = logger;
            return this;
        }

        public void execute() throws SQLException, IOException {
            var sqlUpdater = new SqlUpdater(source, versionTable, replacements, version, logger, type);
            sqlUpdater.init();
        }
    }
}
