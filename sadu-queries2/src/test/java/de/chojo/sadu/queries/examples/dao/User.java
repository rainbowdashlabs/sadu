/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.examples.dao;

import de.chojo.sadu.wrapper.util.Row;

import java.sql.SQLException;
import java.util.UUID;

public record User(int id, UUID uuid, String name) {
    public static User map(Row row) throws SQLException {
        return new User(row.getInt("id"), row.getUuidFromString("uuid"), row.getString("name"));
    }

}
