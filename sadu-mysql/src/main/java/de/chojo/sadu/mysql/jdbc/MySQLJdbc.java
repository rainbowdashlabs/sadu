/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mysql.jdbc;

import de.chojo.sadu.core.jdbc.RemoteJdbcConfig;

/**
 * A builder to create a MySQL jdbc url.
 */
public class MySQLJdbc extends RemoteJdbcConfig<MySQLJdbc> {
    @Override
    public String driver() {
        return "mysql";
    }

    /**
     * The connection timeout value, in milliseconds, or zero for no timeout.
     * Default: 0.
     *
     * @param millis milliseconds
     * @return builder instance
     */
    public MySQLJdbc connectTimeout(int millis) {
        return addParameter("connectTimeout", millis);
    }

    @Override
    protected String defaultDriverClass() {
        return "com.mysql.jdbc.Driver";
    }

    /**
     * @see <a href="https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-configuration-properties.html/">MySql parameter</a>
     */
    @Override
    public <V> MySQLJdbc addParameter(String key, V value) {
        return super.addParameter(key, value);
    }
}
