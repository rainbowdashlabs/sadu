/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.examples.dao;

import de.chojo.sadu.mapper.rowmapper.RowMapping;

import java.util.UUID;

public record User(int id, UUID uuid, String name) {
    public static RowMapping<User> map() {
        return row -> new User(row.getInt("id"), row.getUuidFromString("uuid"), row.getString("name"));
    }
}
