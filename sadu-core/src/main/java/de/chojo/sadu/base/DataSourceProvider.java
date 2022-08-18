/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
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
