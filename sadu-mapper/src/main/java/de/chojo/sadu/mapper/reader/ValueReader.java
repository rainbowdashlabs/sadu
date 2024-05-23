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

/**
 * Definition of a ValueReader to read columns from a {@link Row} and parse it to a java type.
 *
 * @param <V>
 * @param <T>
 * @see StandardReader
 */
public interface ValueReader<V, T> {
    T parse(V value);

    ThrowingBiFunction<Row, String, V, SQLException> namedReader();

    ThrowingBiFunction<Row, Integer, V, SQLException> indexedReader();

    static <V, T> ValueReader<V, T> create(Function<V, T> parser,
                                           ThrowingBiFunction<Row, String, V, SQLException> nameReader,
                                           ThrowingBiFunction<Row, Integer, V, SQLException> indexReader) {
        return new ValueReader<>() {
            @Override
            public T parse(@NotNull V value) {
                return parser.apply(value);
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
}
