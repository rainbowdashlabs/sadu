package de.chojo.sadu.queries.call.adapter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface AdapterMapping<T> {
    void apply(PreparedStatement stmt, int index, T value) throws SQLException;
}
