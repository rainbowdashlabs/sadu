/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.query;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppendedQueryImplTest {
    @Test
    void keepsPercentSignsWithoutFormatArguments() {
        var query = new AppendedQueryImpl(null).query("SELECT * FROM users WHERE name LIKE 'a%'");

        assertEquals("SELECT * FROM users WHERE name LIKE 'a%'", query.sql().sql());
    }

    @Test
    void formatsWithFormatArguments() {
        var query = new AppendedQueryImpl(null).query("SELECT * FROM %s", "users");

        assertEquals("SELECT * FROM users", query.sql().sql());
    }
}
