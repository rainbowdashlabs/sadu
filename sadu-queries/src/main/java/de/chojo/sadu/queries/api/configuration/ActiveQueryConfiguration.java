/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.configuration;

import de.chojo.sadu.queries.api.configuration.context.QueryContext;

import java.sql.SQLException;

public interface ActiveQueryConfiguration extends QueryConfiguration {
    /**
     * Handles a SQLException by invoking the exceptionHandler consumer, logging the exception,
     * and potentially throwing a WrappedQueryExecutionException.
     *
     * @param e the SQLException to handle
     */
    void handleException(SQLException e);

    QueryContext context();
}
