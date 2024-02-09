/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.updater;

import de.chojo.sadu.core.databases.Database;
import de.chojo.sadu.core.jdbc.JdbcConfig;
import de.chojo.sadu.core.updater.SqlVersion;
import de.chojo.sadu.core.updater.UpdaterBuilder;
import org.jetbrains.annotations.CheckReturnValue;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Class to build a {@link SqlUpdater} with a builder pattern
 *
 * @param <T> The type of the jdbc link defined by the {@link Database}
 */
public class BaseSqlUpdaterBuilder<T extends JdbcConfig<?>, S extends BaseSqlUpdaterBuilder<T, ?>> implements UpdaterBuilder<T, S> {
    protected DataSource source;
    protected SqlVersion version;
    protected Map<SqlVersion, Consumer<Connection>> preUpdateHook = new HashMap<>();
    protected Map<SqlVersion, Consumer<Connection>> postUpdateHook = new HashMap<>();
    protected final Database<T, S> type;
    protected String versionTable = "version";
    protected QueryReplacement[] replacements = new QueryReplacement[0];
    protected ClassLoader classLoader = getClass().getClassLoader();

    public BaseSqlUpdaterBuilder(Database<T, S> type) {
        this.type = type;
    }

    public S setSource(DataSource source) {
        this.source = source;
        return self();
    }

    public S setVersion(SqlVersion version) {
        this.version = version;
        return self();
    }

    /**
     * The version table which contains major and patch version.
     * <p>
     * Changing this later might cause a loss of data.
     *
     * @param versionTable name of the version table
     * @return builder instance
     */
    @CheckReturnValue
    public S setVersionTable(String versionTable) {
        this.versionTable = versionTable;
        return self();
    }

    /**
     * Replacements which should be applied to the executed scripts. Can be used to change schema names
     * or other variables during deployment
     *
     * @param replacements replacements
     * @return builder instance
     */
    @CheckReturnValue
    public S setReplacements(QueryReplacement... replacements) {
        this.replacements = replacements;
        return self();
    }

    public S preUpdateHook(SqlVersion version, Consumer<Connection> consumer) {
        preUpdateHook.put(version, consumer);
        return self();
    }

    public S postUpdateHook(SqlVersion version, Consumer<Connection> consumer) {
        postUpdateHook.put(version, consumer);
        return self();
    }


    @Override
    public S withClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return self();
    }

    /**
     * Build the updater and start the update process.
     *
     * @throws SQLException If execution of the scripts fails
     * @throws IOException  If the scripts can't be read.
     */
    public void execute() throws SQLException, IOException {
        if (version == null) version = SqlVersion.load(classLoader);
        var sqlUpdater = new SqlUpdater<>(source, versionTable, replacements, version, type, preUpdateHook, postUpdateHook, classLoader);
        sqlUpdater.init();
    }

    /**
     * Returns the instance with the correct type.
     *
     * @return instance
     */
    @SuppressWarnings("unchecked")
    protected S self() {
        return (S) this;
    }
}
