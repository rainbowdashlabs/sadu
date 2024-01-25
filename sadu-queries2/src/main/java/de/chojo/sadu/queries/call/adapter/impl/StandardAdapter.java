package de.chojo.sadu.queries.call.adapter.impl;

import de.chojo.sadu.queries.call.adapter.Adapter;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;

import static de.chojo.sadu.queries.call.adapter.Adapter.create;

public final class StandardAdapter {
    public static final Adapter<String> STRING = create(String.class, PreparedStatement::setString, Types.VARCHAR);
    public static final Adapter<Integer> INTEGER = create(Integer.class, PreparedStatement::setInt, Types.INTEGER);
    public static final Adapter<Float> FLOAT = create(Float.class, PreparedStatement::setFloat, Types.FLOAT);
    public static final Adapter<Double> DOUBLE = create(Double.class, PreparedStatement::setDouble, Types.DOUBLE);
    public static final Adapter<Long> LONG = create(Long.class, PreparedStatement::setLong, Types.BIGINT);
    public static final Adapter<Boolean> BOOLEAN = create(Boolean.class, PreparedStatement::setBoolean, Types.BOOLEAN);
    public static final Adapter<LocalDate> LOCAL_DATE = create(LocalDate.class, (stmt, index, value) -> stmt.setDate(index, Date.valueOf(value)), Types.DATE);

    private StandardAdapter() {
        throw new UnsupportedOperationException("This is a utility class.");
    }
}
