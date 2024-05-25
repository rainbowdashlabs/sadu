/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.reader;

import de.chojo.sadu.core.exceptions.ThrowingBiFunction;
import de.chojo.sadu.mapper.wrapper.Row;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Definition of a ValueReader to read columns from a {@link Row} and parse it to a java type.
 *
 * @param <T> The java type that is returned by the adapter
 * @param <V> The intermediate SQL type that is retrieved from the row
 * @see StandardReader
 */
public interface ValueReader<T, V> {
    static <V, T> ValueReader<T, V> create(Function<V, T> parser,
                                           ThrowingBiFunction<Row, String, V, SQLException> nameReader,
                                           ThrowingBiFunction<Row, Integer, V, SQLException> indexReader) {
        return new ValueReader<>() {
            @Override
            public Function<@NotNull V, T> reader() {
                return parser;
            }

            @Override
            public ThrowingBiFunction<Row, String, V, SQLException> namedReader() {
                return nameReader;
            }

            @Override
            public ThrowingBiFunction<Row, Integer, V, SQLException> indexedReader() {
                return indexReader;
            }
        };
    }

    static <V, T> ValueReader<T, V> create(Function<V, T> parser,
                                           ThrowingBiFunction<Row, String, V, SQLException> nameReader,
                                           ThrowingBiFunction<Row, Integer, V, SQLException> indexReader,
                                           Supplier<T> defaultValue) {
        return new ValueReader<>() {
            @Override
            public Function<@NotNull V, T> reader() {
                return parser;
            }

            @Override
            public ThrowingBiFunction<Row, String, V, SQLException> namedReader() {
                return nameReader;
            }

            @Override
            public ThrowingBiFunction<Row, Integer, V, SQLException> indexedReader() {
                return indexReader;
            }

            @Override
            public T defaultValue() {
                return defaultValue.get();
            }
        };
    }

    /**
     * A reader that takes the sql type and converts it into a java type.
     * The passed sql type instance is never null.
     *
     * @return a converted java instance of the sql object
     */
    Function<@NotNull V, T> reader();

    /**
     * Function that provides access to a reader that returns the column value via column name
     *
     * @return function
     */
    ThrowingBiFunction<Row, String, V, SQLException> namedReader();

    /**
     * Function that provides access to a reader that returns the column value via column index
     *
     * @return function
     */
    ThrowingBiFunction<Row, Integer, V, SQLException> indexedReader();

    /**
     * A default value that should be returned when the SQL value is null
     *
     * @return a default value or null
     */
    default T defaultValue() {
        return null;
    }
}
