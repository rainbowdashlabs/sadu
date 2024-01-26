/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.base;

import de.chojo.sadu.base.DataSourceProvider;
import de.chojo.sadu.exceptions.ThrowingFunction;
import de.chojo.sadu.queries.configuration.QueryConfiguration;
import de.chojo.sadu.queries.stages.Query;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

public interface QueryProvider extends ConnectionProvider, DataSourceProvider {
    Query query();

    @Override
    default DataSource source() {
        return query().source();
    }

    default QueryConfiguration configuration() {
        return query().configuration();
    }

    default <T> T callConnection(Supplier<T> defaultResult, ThrowingFunction<T, Connection, SQLException> connectionConsumer) {
        return query().callConnection(defaultResult, connectionConsumer);
    }
}
