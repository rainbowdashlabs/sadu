/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.base;

import de.chojo.sadu.base.DataSourceProvider;
import de.chojo.sadu.exceptions.ThrowingFunction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

public interface ConnectionProvider extends DataSourceProvider {
    <T> T callConnection(Supplier<T> defaultResult, ThrowingFunction<T, Connection, SQLException> connectionConsumer);
}
