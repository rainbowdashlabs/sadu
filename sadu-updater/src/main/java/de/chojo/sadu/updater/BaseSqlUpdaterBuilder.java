/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.updater;

import de.chojo.sadu.base.QueryFactory;
import de.chojo.sadu.databases.Database;
import de.chojo.sadu.jdbc.JdbcConfig;
import de.chojo.sadu.wrapper.QueryBuilderConfig;

import javax.annotation.CheckReturnValue;
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
    protected QueryBuilderConfig config = QueryBuilderConfig.builder().throwExceptions().build();

    public BaseSqlUpdaterBuilder(Database<T, S> type) {
        this.type = type;
    }

    public void setSource(DataSource source) {
        this.source = source;
    }

    public void setVersion(SqlVersion version) {
        this.version = version;
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

    /**
     * Set the {@link QueryBuilderConfig} for the underlying {@link QueryFactory}
     *
     * @param config config so apply
     * @return builder instance
     */
    @CheckReturnValue
    public S withConfig(QueryBuilderConfig config) {
        this.config = config;
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

    /**
     * Build the updater and start the update process.
     *
     * @throws SQLException If execution of the scripts fails
     * @throws IOException  If the scripts can't be read.
     */
    public void execute() throws SQLException, IOException {
        var sqlUpdater = new SqlUpdater<>(source, config, versionTable, replacements, version, type, preUpdateHook, postUpdateHook);
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
