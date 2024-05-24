package de.chojo.sadu.queries.converter;

import de.chojo.sadu.core.exceptions.ThrowingBiFunction;
import de.chojo.sadu.mapper.reader.ValueReader;
import de.chojo.sadu.mapper.wrapper.Row;
import de.chojo.sadu.queries.api.call.adapter.Adapter;
import de.chojo.sadu.queries.api.call.adapter.AdapterMapping;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Function;

/**
 * A value converter that serves as an {@link AdapterMapping} and {@link ValueReader}
 * @param <T> The Java type that is handled
 * @param <V> The intermediate SQL type that is sent to the database
 */
public interface ValueConverter<T, V> extends Adapter<T>, ValueReader<T, V> {
    static <T, V> ValueConverter<T, V> create(Adapter<T> mapper, ValueReader<T, V> reader) {
        return new ValueConverter<>() {


            @Override
            public Function<@NotNull V, T> reader() {
                return reader.reader();
            }

            @Override
            public ThrowingBiFunction<Row, String, V, SQLException> namedReader() {
                return reader.namedReader();
            }

            @Override
            public ThrowingBiFunction<Row, Integer, V, SQLException> indexedReader() {
                return reader.indexedReader();
            }

            @Override
            public AdapterMapping<T> mapping() {
                return mapper.mapping();
            }

            @Override
            public int type() {
                return mapper.type();
            }

            @Override
            public void apply(PreparedStatement stmt, int index, T value) throws SQLException {
                mapper.apply(stmt, index, value);
            }
        };
    }
}
