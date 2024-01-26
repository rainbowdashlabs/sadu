/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.params;

import de.chojo.sadu.queries.TokenizedQuery;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface BaseParam {
    void apply(TokenizedQuery query, PreparedStatement stmt) throws SQLException;
}
