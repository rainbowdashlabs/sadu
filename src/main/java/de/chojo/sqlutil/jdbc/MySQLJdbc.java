/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil.jdbc;

public class MySQLJdbc extends RemoteJdbcConfig<MySQLJdbc> {
    @Override
    public String driver() {
        return "mysql";
    }

    /**
     * The connect timeout value, in milliseconds, or zero for no timeout.
     * Default: 0.
     *
     * @param millis milliseconds
     * @return builder instance
     */
    public MySQLJdbc connectTimeout(int millis) {
        return addParameter("connectTimeout", millis);
    }


    /**
     * {@inheritDoc}
     *
     * @see <a href="https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-configuration-properties.html/">MySql parameter</a>
     */
    @Override
    public <V> MySQLJdbc addParameter(String key, V value) {
        return super.addParameter(key, value);
    }
}
