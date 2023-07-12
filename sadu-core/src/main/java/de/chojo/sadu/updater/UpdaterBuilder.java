/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.updater;

import de.chojo.sadu.jdbc.JdbcConfig;
import org.jetbrains.annotations.ApiStatus;

import javax.sql.DataSource;

@ApiStatus.Internal
public interface UpdaterBuilder<T extends JdbcConfig<?>, S extends UpdaterBuilder<T, ?>> {
    void setSource(DataSource source);
    void setVersion(SqlVersion version);
}
