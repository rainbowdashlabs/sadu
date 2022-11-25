/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.params;

import de.chojo.sadu.queries.TokenizedQuery;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface BaseParam {
    void apply(TokenizedQuery query, PreparedStatement stmt) throws SQLException;
}
