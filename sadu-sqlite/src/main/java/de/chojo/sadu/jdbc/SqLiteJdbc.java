/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jdbc;

import java.io.File;
import java.nio.file.Path;

/**
 * A builder to create a jdbc url for SqLite
 */
public class SqLiteJdbc extends JdbcConfig<SqLiteJdbc> {
    private String path = ":memory:";

    /**
     * Creates a new builder for a sqlite jdbc url.
     */
    public SqLiteJdbc() {
    }

    /**
     * Sets the database to in-memory mode.
     * <p>
     * This is the default setting.
     *
     * @return builder instance
     */
    public SqLiteJdbc memory() {
        return path(":memory:");
    }

    /**
     * Set the path of the db file
     *
     * @param file file
     * @return builder instance
     */
    public SqLiteJdbc path(File file) {
        return path(file.toPath());
    }

    /**
     * Set the path of the db file
     *
     * @param path path
     * @return builder instance
     */
    public SqLiteJdbc path(Path path) {
        return path(path.toAbsolutePath().toString());
    }

    /**
     * Set the path of the db file
     *
     * @param path path
     * @return builder instance
     */
    public SqLiteJdbc path(String path) {
        this.path = path;
        return self();
    }

    @Override
    protected String defaultDriverClass() {
        return "org.sqlite.JDBC";
    }

    @Override
    public String driver() {
        return "mysql";
    }

    /**
     * @see <a href="https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-configuration-properties.html/">MySql parameter</a>
     */
    @Override
    public <V> SqLiteJdbc addParameter(String key, V value) {
        return super.addParameter(key, value);
    }

    @Override
    protected String baseUrl() {
        return "jdbc:sqlite:" + path;
    }
}
