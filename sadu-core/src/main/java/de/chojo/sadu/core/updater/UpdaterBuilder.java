/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.core.updater;

import de.chojo.sadu.core.jdbc.JdbcConfig;
import org.jetbrains.annotations.ApiStatus;

import javax.sql.DataSource;

@ApiStatus.Internal
public interface UpdaterBuilder<T extends JdbcConfig<?>, S extends UpdaterBuilder<T, ?>> {
    /**
     * Set the datasource that should be used
     *
     * @param source source
     */
    S setSource(DataSource source);

    /**
     * Set the current db version that is expected
     *
     * @param version version
     */
    S setVersion(SqlVersion version);

    /**
     * Set the Classloader that should be used to load resourced.
     *
     * @param classLoader classloader
     */
    S withClassLoader(ClassLoader classLoader);
}
