/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.exception;

import de.chojo.sadu.mapper.wrapper.Row;
import de.chojo.sadu.queries.query.TokenizedQuery;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.util.Set;

public final class Check {
    public static void assertQueryResult(@Nullable Object object) {
        if (object instanceof Row) {
            throw new IllegalQueryReturnTypeException("Result is of type Row. Using a row mapper to map to a Row is forbidden. Rows are mutable and are no longer accessible after query execution.");
        }
        if (object instanceof ResultSet) {
            throw new IllegalQueryReturnTypeException("Result is of type ResultSet. Using a row mapper to map to a ResultSet is forbidden. ResultSets are mutable and are no longer accessible after query execution.");
        }
    }

    public static void assertIndexRange(int index, TokenizedQuery query) {
        if (query.indexSize() >= index) return;
        throw new IllegalQueryParameterException("No parameter with index %s exists in query \"%s\". Only %s parameter(s) is/are defined".formatted(index, query.sql(), query.indexSize()));
    }

    public static void assertIndexFilled(int count, TokenizedQuery query) {
        if (query.indexSize() == count) return;
        throw new IllegalQueryParameterException("Missing index parameter. %s were given, but %s were set in query: \"%s\"".formatted(count, query.indexSize(), query.sql()));
    }

    public static void missingToken(Set<String> tokens, TokenizedQuery query) {
        if (tokens.isEmpty()) return;
        throw new IllegalQueryParameterException("The parameters %s are not bound in query: %s".formatted(String.join(", ", tokens), query.sql()));
    }
}
