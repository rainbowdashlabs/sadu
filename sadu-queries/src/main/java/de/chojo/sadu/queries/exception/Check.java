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
        if (query.indexSize() >= index) {
            throw new IllegalQueryParameterException("No parameter with index %s exists in query \"%s\". Only %s parameters are defined".formatted(index, query.sql(), query.indexSize()));
        }
    }

    public static void assertIndexFilled(int count, TokenizedQuery query) {
        if (query.indexSize() != count) {
            throw new IllegalQueryParameterException("Missing index parameter. %s were given, but %s were set in query: \"%s\"".formatted(count, query.indexSize(), query.sql()));
        }
    }
}
