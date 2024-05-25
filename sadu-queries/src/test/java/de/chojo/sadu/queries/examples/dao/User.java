/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.examples.dao;

import de.chojo.sadu.mapper.rowmapper.RowMapping;

import java.util.UUID;

import static de.chojo.sadu.mapper.reader.StandardReader.UUID_FROM_STRING;

public record User(int id, UUID uuid, String name) {
    public static RowMapping<User> map() {
        return row -> new User(row.getInt("id"), row.get("uuid", UUID_FROM_STRING), row.getString("name"));
    }
}
