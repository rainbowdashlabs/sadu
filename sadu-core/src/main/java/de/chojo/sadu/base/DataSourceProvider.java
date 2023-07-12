/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.base;

import javax.sql.DataSource;

/**
 * A class which can provide a datasource
 */
public interface DataSourceProvider {
    /**
     * Get the underlying datasource
     *
     * @return datasource
     */
    DataSource source();
}
