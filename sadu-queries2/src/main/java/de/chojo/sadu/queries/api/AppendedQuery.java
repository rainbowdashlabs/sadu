/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api;

import org.intellij.lang.annotations.Language;

public interface AppendedQuery {
    /**
     * Execute another query with the same chain.
     *
     * @param sql    the query you want to execute
     * @param format will work like calling {@link String#formatted(Object...)}.
     *               <b>DO NOT USE THAT FOR PARAMETER OR WITH USER INPUT</b>
     * @return a parsed query
     */
    ParsedQuery query(@Language("sql") String sql, Object... format);
}
