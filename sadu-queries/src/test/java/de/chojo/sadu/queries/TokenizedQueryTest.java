/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries;

import de.chojo.sadu.queries.query.TokenizedQuery;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenizedQueryTest {
    private final String sql = "INSERT INTO persons VALUES(:name, ?,:age::integer, ?, ?,:gender,:gender);";
    private final TokenizedQuery tokenizedQuery = TokenizedQuery.create(sql);

    @Test
    void create() {
        TokenizedQuery.create(sql);
    }

    @Test
    void getNamedTokenIndex() {
        assertEquals(Collections.singletonList(1), tokenizedQuery.getNamedTokenIndex(":name"));
        assertEquals(Collections.singletonList(3), tokenizedQuery.getNamedTokenIndex(":age"));
        assertEquals(List.of(6, 7), tokenizedQuery.getNamedTokenIndex(":gender"));
    }

    @Test
    void getIndexTokenIndex() {
        assertEquals(2, tokenizedQuery.getIndexTokenIndex(1));
        assertEquals(4, tokenizedQuery.getIndexTokenIndex(2));
        assertEquals(5, tokenizedQuery.getIndexTokenIndex(3));
    }

    @Test
    void sql() {
        assertEquals(sql, tokenizedQuery.sql());
    }

    @Test
    void tokenizedSql() {
        assertEquals("INSERT INTO persons VALUES(?, ?,?::integer, ?, ?,?,?);", tokenizedQuery.tokenizedSql());
    }
}
