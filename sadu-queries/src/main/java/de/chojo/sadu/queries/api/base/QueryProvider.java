/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.base;

import de.chojo.sadu.core.base.DataSourceProvider;
import de.chojo.sadu.core.exceptions.ThrowingFunction;
import de.chojo.sadu.queries.api.configuration.QueryConfiguration;
import de.chojo.sadu.queries.query.QueryImpl;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface QueryProvider extends ConnectionProvider, DataSourceProvider {
    QueryImpl query();

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
