/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.base;

import de.chojo.sadu.base.DataSourceProvider;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider extends DataSourceProvider {
    Connection connection() throws SQLException;
}
