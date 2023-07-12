/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.util;

import de.chojo.sadu.conversion.ArrayConverter;
import de.chojo.sadu.conversion.UUIDConverter;
import de.chojo.sadu.types.SqlType;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.JDBCType;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.UUID;

/**
 * A class wrapping a {@link PreparedStatement} and allows to set the values with a builder pattern.
 * <p>
 * The index of the argument will be moved automatically
 */
@SuppressWarnings("unused")
public class ParamBuilder {
    private final PreparedStatement stmt;
    private int index = 1;

    /**
     * Create a new ParamBuilder
     *
     * @param stmt statement to wrap
     */
    public ParamBuilder(PreparedStatement stmt) {
        this.stmt = stmt;
    }

    private int index() {
        return index++;
    }

    /**
     * Sets the designated parameter to SQL {@code NULL}.
     *
     * <P><B>Note:</B> You must specify the parameter's SQL type.
     *
     * @param sqlType the SQL type code defined in {@code java.sql.Types}
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed {@code PreparedStatement}
     * @throws SQLFeatureNotSupportedException if {@code sqlType} is
     *                                         a {@code ARRAY}, {@code BLOB}, {@code CLOB},
     *                                         {@code DATALINK}, {@code JAVA_OBJECT}, {@code NCHAR},
     *                                         {@code NCLOB}, {@code NVARCHAR}, {@code LONGNVARCHAR},
     *                                         {@code REF}, {@code ROWID}, {@code SQLXML}
     *                                         or  {@code STRUCT} data type and the JDBC driver does not support
     *                                         this data type
     */
    public ParamBuilder setNull(int sqlType) throws SQLException {
        stmt.setNull(index(), sqlType);
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code java.sql.Array} object.
     * The driver converts this to an SQL {@code ARRAY} value when it
     * sends it to the database.
     *
     * @param list A {@code Collection} that maps an SQL {@code ARRAY} value
     * @param type The type of the sql column.
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed {@code PreparedStatement}
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.2
     */
    public ParamBuilder setArray(@NotNull Collection<?> list, SqlType type) throws SQLException {
        stmt.setArray(index(), ArrayConverter.toSqlArray(stmt.getConnection(), type, list));
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code java.sql.Array} object.
     * The driver converts this to an SQL {@code ARRAY} value when it
     * sends it to the database.
     *
     * @param array An {@code Array} object that maps an SQL {@code ARRAY} value
     * @param type  An
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed {@code PreparedStatement}
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.2
     */
    public ParamBuilder setArray(@NotNull Object[] array, SqlType type) throws SQLException {
        stmt.setArray(index(), ArrayConverter.toSqlArray(stmt.getConnection(), type, array));
        return this;
    }

    /**
     * Sets the designated parameter to the given Java {@code boolean} value.
     * The driver converts this
     * to an SQL {@code BIT} or {@code BOOLEAN} value when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement;
     *                      if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setBoolean(Boolean x) throws SQLException {
        if (x == null) return setNull(Types.BOOLEAN);
        stmt.setBoolean(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given Java {@code byte} value.
     * The driver converts this
     * to an SQL {@code TINYINT} value when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setByte(Byte x) throws SQLException {
        if (x == null) return setNull(Types.BIT);
        stmt.setByte(index(), x);
        return this;
    }

    /**
     * Sets the uuid as a byte value.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setUuidAsBytes(UUID x) throws SQLException {
        if (x == null) return setNull(Types.BIT);
        stmt.setBytes(index(), UUIDConverter.convert(x));
        return this;
    }

    /**
     * Sets the uuid as a string value.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setUuidAsString(UUID x) throws SQLException {
        if (x == null) return setNull(Types.BIT);
        stmt.setString(index(), x.toString());
        return this;
    }

    /**
     * Sets the designated parameter to the given Java {@code short} value.
     * The driver converts this
     * to an SQL {@code SMALLINT} value when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setShort(Short x) throws SQLException {
        if (x == null) return setNull(Types.INTEGER);
        stmt.setShort(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given Java {@code int} value.
     * The driver converts this
     * to an SQL {@code INTEGER} value when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setInt(Integer x) throws SQLException {
        if (x == null) return setNull(Types.INTEGER);
        stmt.setInt(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given Java {@code long} value.
     * The driver converts this
     * to an SQL {@code BIGINT} value when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setLong(Long x) throws SQLException {
        if (x == null) return setNull(Types.BIGINT);
        stmt.setLong(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given Java {@code float} value.
     * The driver converts this
     * to an SQL {@code REAL} value when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setFloat(Float x) throws SQLException {
        if (x == null) return setNull(Types.FLOAT);
        stmt.setFloat(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given Java {@code double} value.
     * The driver converts this
     * to an SQL {@code DOUBLE} value when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setDouble(Double x) throws SQLException {
        if (x == null) return setNull(Types.DOUBLE);
        stmt.setDouble(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code java.math.BigDecimal} value.
     * The driver converts this to an SQL {@code NUMERIC} value when
     * it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setBigDecimal(BigDecimal x) throws SQLException {
        if (x == null) return setNull(Types.DECIMAL);
        stmt.setBigDecimal(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given Java {@code String} value.
     * The driver converts this
     * to an SQL {@code VARCHAR} or {@code LONGVARCHAR} value
     * (depending on the argument's
     * size relative to the driver's limits on {@code VARCHAR} values)
     * when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setString(String x) throws SQLException {
        if (x == null) return setNull(Types.VARCHAR);
        stmt.setString(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given Java {@code Enum} name value.
     * The driver converts this
     * to an SQL {@code VARCHAR} or {@code LONGVARCHAR} value
     * (depending on the argument's
     * size relative to the driver's limits on {@code VARCHAR} values)
     * when it sends it to the database.
     *
     * @param x the parameter value
     * @param <T> Type of enum
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public <T extends Enum<?>> ParamBuilder setEnum(T x) throws SQLException {
        if (x == null) return setNull(Types.VARCHAR);
        stmt.setString(index(), x.name());
        return this;
    }

    /**
     * Sets the designated parameter to the given Java array of bytes.  The driver converts
     * this to an SQL {@code VARBINARY} or {@code LONGVARBINARY}
     * (depending on the argument's size relative to the driver's limits on
     * {@code VARBINARY} values) when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setBytes(byte[] x) throws SQLException {
        if (x == null) return setNull(Types.BINARY);
        stmt.setBytes(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code java.sql.Date} value
     * using the default time zone of the virtual machine that is running
     * the application.
     * The driver converts this
     * to an SQL {@code DATE} value when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setDate(Date x) throws SQLException {
        if (x == null) return setNull(Types.DATE);
        stmt.setDate(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code java.time.LocalDate} value
     * using the default time zone of the virtual machine that is running
     * the application.
     * The driver converts this
     * to an SQL {@code DATE} value when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setLocalDate(LocalDate x) throws SQLException {
        if (x == null ) return setNull(Types.DATE);
        stmt.setDate(index(), Date.valueOf(x));
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code java.sql.Time} value.
     * The driver converts this
     * to an SQL {@code TIME} value when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setTime(Time x) throws SQLException {
        if (x == null) return setNull(Types.TIME);
        stmt.setTime(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code java.time.LocalTime} value.
     * The driver converts this
     * to an SQL {@code TIME} value when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setLocalTime(LocalTime x) throws SQLException {
        if (x == null) return setNull(Types.TIME);
        stmt.setTime(index(), Time.valueOf(x));
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code java.sql.Timestamp} value.
     * The driver
     * converts this to an SQL {@code TIMESTAMP} value when it sends it to the
     * database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setTimestamp(Timestamp x) throws SQLException {
        if (x == null) return setNull(Types.TIMESTAMP);
        stmt.setTimestamp(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code java.time.LocalDateTime} value.
     * The driver
     * converts this to an SQL {@code TIMESTAMP} value when it sends it to the
     * database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setLocalDateTime(LocalDateTime x) throws SQLException {
        if (x == null) return setNull(Types.TIMESTAMP);
        stmt.setTimestamp(index(), Timestamp.valueOf(x));
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code java.time.ZonedDateTime} value.
     * The driver
     * converts this to an SQL {@code TIMESTAMP} value when it sends it to the
     * database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setZonedDateTime(ZonedDateTime x) throws SQLException {
        if (x == null) return setNull(Types.TIMESTAMP);
        stmt.setTimestamp(index(), Timestamp.valueOf(x.toLocalDateTime()));
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code java.time.OffsetDateTime} value.
     * The driver
     * converts this to an SQL {@code TIMESTAMP} value when it sends it to the
     * database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setOffsetDateTime(OffsetDateTime x) throws SQLException {
        if (x == null) return setNull(Types.TIMESTAMP);
        stmt.setTimestamp(index(), Timestamp.valueOf(x.toLocalDateTime()));
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code java.time.OffsetTime} value.
     * The driver
     * converts this to an SQL {@code TIMESTAMP} value when it sends it to the
     * database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setOffsetTime(OffsetTime x) throws SQLException {
        if (x == null) return setNull(Types.TIME);
        stmt.setTime(index(), Time.valueOf(x.toLocalTime()));
        return this;
    }

    /**
     * Sets the designated parameter to the given input stream, which will have
     * the specified number of bytes.
     * When a very large ASCII value is input to a {@code LONGVARCHAR}
     * parameter, it may be more practical to send it via a
     * {@code java.io.InputStream}. Data will be read from the stream
     * as needed until end-of-file is reached.  The JDBC driver will
     * do any necessary conversion from ASCII to the database char format.
     *
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     *
     * @param x      the Java input stream that contains the ASCII parameter value
     * @param length the number of bytes in the stream
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setAsciiStream(InputStream x, int length) throws SQLException {
        if (x == null) return setNull(Types.VARCHAR);
        stmt.setAsciiStream(index(), x, length);
        return this;
    }

    /**
     * Sets the designated parameter to the given input stream, which will have
     * the specified number of bytes.
     * When a very large binary value is input to a {@code LONGVARBINARY}
     * parameter, it may be more practical to send it via a
     * {@code java.io.InputStream} object. The data will be read from the
     * stream as needed until end-of-file is reached.
     *
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     *
     * @param x      the java input stream which contains the binary parameter value
     * @param length the number of bytes in the stream
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     */
    public ParamBuilder setBinaryStream(InputStream x, int length) throws SQLException {
        if (x == null) return setNull(Types.VARCHAR);
        stmt.setBinaryStream(index(), x, length);
        return this;
    }

    /**
     * Sets the value of the designated parameter with the given object.
     * <p>
     * This method is similar to {@link #setObject(Object x, int targetSqlType, int scaleOrLength)},
     * except that it assumes a scale of zero.
     *
     * @param x             the object containing the input parameter value
     * @param targetSqlType the SQL type (as defined in java.sql.Types) to be
     *                      sent to the database
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or this
     *                                         method is called on a closed PreparedStatement
     * @throws SQLFeatureNotSupportedException if
     *                                         the JDBC driver does not support the specified targetSqlType
     * @see Types
     */
    public ParamBuilder setObject(Object x, int targetSqlType) throws SQLException {
        if (x == null) return setNull(targetSqlType);
        stmt.setObject(index(), x, targetSqlType);
        return this;
    }

    /**
     * <p>Sets the value of the designated parameter using the given object.
     *
     * <p>The JDBC specification specifies a standard mapping from
     * Java {@code Object} types to SQL types.  The given argument
     * will be converted to the corresponding SQL type before being
     * sent to the database.
     *
     * <p>Note that this method may be used to pass database-specific
     * abstract data types, by using a driver-specific Java type.
     * <p>
     * If the object is of a class implementing the interface {@code SQLData},
     * the JDBC driver should call the method {@code SQLData.writeSQL}
     * to write it to the SQL data stream.
     * If, on the other hand, the object is of a class implementing
     * {@code Ref}, {@code Blob}, {@code Clob},  {@code NClob},
     * {@code Struct}, {@code java.net.URL}, {@code RowId}, {@code SQLXML}
     * or {@code Array}, the driver should pass it to the database as a
     * value of the corresponding SQL type.
     * <p>
     * <b>Note:</b> Not all databases allow for a non-typed Null to be sent to
     * the backend. For maximum portability, the {@code setNull} or the
     * {@code setObject(Object x, int sqlType)}
     * method should be used
     * instead of {@code setObject(Object x)}.
     * <p>
     * <b>Note:</b> This method throws an exception if there is an ambiguity, for example, if the
     * object is of a class implementing more than one of the interfaces named above.
     *
     * @param x the object containing the input parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs;
     *                      this method is called on a closed {@code PreparedStatement}
     *                      or the type of the given object is ambiguous
     */
    public ParamBuilder setObject(Object x) throws SQLException {
        stmt.setObject(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code Reader}
     * object, which is the given number of characters long.
     * When a very large UNICODE value is input to a {@code LONGVARCHAR}
     * parameter, it may be more practical to send it via a
     * {@code java.io.Reader} object. The data will be read from the stream
     * as needed until end-of-file is reached.  The JDBC driver will
     * do any necessary conversion from UNICODE to the database char format.
     *
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     *
     * @param reader the {@code java.io.Reader} object that contains the
     *               Unicode data
     * @param length the number of characters in the stream
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     * @since 1.2
     */
    public ParamBuilder setCharacterStream(Reader reader, int length) throws SQLException {
        if (reader == null) return setNull(Types.VARCHAR);
        stmt.setCharacterStream(index(), reader, length);
        return this;
    }

    /**
     * Sets the designated parameter to the given
     * {@code REF(&lt;structured-type&gt;)} value.
     * The driver converts this to an SQL {@code REF} value when it
     * sends it to the database.
     *
     * @param x an SQL {@code REF} value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed {@code PreparedStatement}
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.2
     */
    public ParamBuilder setRef(Ref x) throws SQLException {
        if (x == null) return setNull(Types.REF);
        stmt.setRef(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code java.sql.Blob} object.
     * The driver converts this to an SQL {@code BLOB} value when it
     * sends it to the database.
     *
     * @param x a {@code Blob} object that maps an SQL {@code BLOB} value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed {@code PreparedStatement}
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.2
     */
    public ParamBuilder setBlob(Blob x) throws SQLException {
        if (x == null) return setNull(Types.BLOB);
        stmt.setBlob(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code java.sql.Clob} object.
     * The driver converts this to an SQL {@code CLOB} value when it
     * sends it to the database.
     *
     * @param x a {@code Clob} object that maps an SQL {@code CLOB} value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed {@code PreparedStatement}
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.2
     */
    public ParamBuilder setClob(Clob x) throws SQLException {
        if (x == null) return setNull(Types.CLOB);
        stmt.setClob(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code java.sql.Array} object.
     * The driver converts this to an SQL {@code ARRAY} value when it
     * sends it to the database.
     *
     * @param x an {@code Array} object that maps an SQL {@code ARRAY} value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed {@code PreparedStatement}
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.2
     */
    public ParamBuilder setArray(Array x) throws SQLException {
        if (x == null) return setNull(Types.ARRAY);
        stmt.setArray(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code java.net.URL} value.
     * The driver converts this to an SQL {@code DATALINK} value
     * when it sends it to the database.
     *
     * @param x the {@code java.net.URL} object to be set
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed {@code PreparedStatement}
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.4
     */
    public ParamBuilder setURL(URL x) throws SQLException {
        if (x == null) return setNull(Types.DATALINK);
        stmt.setURL(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code java.sql.RowId} object. The
     * driver converts this to a SQL {@code ROWID} value when it sends it
     * to the database
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed {@code PreparedStatement}
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setRowId(RowId x) throws SQLException {
        if (x == null) return setNull(Types.ROWID);
        stmt.setRowId(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code String} object.
     * The driver converts this to a SQL {@code NCHAR} or
     * {@code NVARCHAR} or {@code LONGNVARCHAR} value
     * (depending on the argument's
     * size relative to the driver's limits on {@code NVARCHAR} values)
     * when it sends it to the database.
     *
     * @param value the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if the driver does not support national
     *                                         character sets;  if the driver can detect that a data conversion
     *                                         error could occur; if a database access error occurs; or
     *                                         this method is called on a closed {@code PreparedStatement}
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setNString(String value) throws SQLException {
        if (value == null) return setNull(Types.NVARCHAR);
        stmt.setNString(index(), value);
        return this;
    }

    /**
     * Sets the designated parameter to a {@code Reader} object. The
     * {@code Reader} reads the data till end-of-file is reached. The
     * driver does the necessary conversion from Java character format to
     * the national character set in the database.
     *
     * @param value  the parameter value
     * @param length the number of characters in the parameter data.
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if the driver does not support national
     *                                         character sets;  if the driver can detect that a data conversion
     *                                         error could occur; if a database access error occurs; or
     *                                         this method is called on a closed {@code PreparedStatement}
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setNCharacterStream(Reader value, long length) throws SQLException {
        if (value == null) return setNull(Types.VARCHAR);
        stmt.setNCharacterStream(index(), value, length);
        return this;
    }

    /**
     * Sets the designated parameter to a {@code java.sql.NClob} object. The driver converts this to a
     * SQL {@code NCLOB} value when it sends it to the database.
     *
     * @param value the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if the driver does not support national
     *                                         character sets;  if the driver can detect that a data conversion
     *                                         error could occur; if a database access error occurs; or
     *                                         this method is called on a closed {@code PreparedStatement}
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setNClob(NClob value) throws SQLException {
        if (value == null) return setNull(Types.NCLOB);
        stmt.setNClob(index(), value);
        return this;
    }

    /**
     * Sets the designated parameter to a {@code Reader} object.  The reader must contain  the number
     * of characters specified by length otherwise a {@code SQLException} will be
     * generated when the {@code PreparedStatement} is executed.
     * This method differs from the {@code setCharacterStream (int, Reader, int)} method
     * because it informs the driver that the parameter value should be sent to
     * the server as a {@code CLOB}.  When the {@code setCharacterStream} method is used, the
     * driver may have to do extra work to determine whether the parameter
     * data should be sent to the server as a {@code LONGVARCHAR} or a {@code CLOB}
     *
     * @param reader An object that contains the data to set the parameter value to.
     * @param length the number of characters in the parameter data.
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs; this method is called on
     *                                         a closed {@code PreparedStatement} or if the length specified is less than zero.
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setClob(Reader reader, long length) throws SQLException {
        if (reader == null) return setNull(Types.VARCHAR);
        stmt.setClob(index(), reader, length);
        return this;
    }

    /**
     * Sets the designated parameter to a {@code InputStream} object.
     * The {@code Inputstream} must contain  the number
     * of characters specified by length otherwise a {@code SQLException} will be
     * generated when the {@code PreparedStatement} is executed.
     * This method differs from the {@code setBinaryStream (int, InputStream, int)}
     * method because it informs the driver that the parameter value should be
     * sent to the server as a {@code BLOB}.  When the {@code setBinaryStream} method is used,
     * the driver may have to do extra work to determine whether the parameter
     * data should be sent to the server as a {@code LONGVARBINARY} or a {@code BLOB}
     *
     * @param inputStream An object that contains the data to set the parameter
     *                    value to.
     * @param length      the number of bytes in the parameter data.
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs;
     *                                         this method is called on a closed {@code PreparedStatement};
     *                                         if the length specified
     *                                         is less than zero or if the number of bytes in the {@code InputStream} does not match
     *                                         the specified length.
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setBlob(InputStream inputStream, long length) throws SQLException {
        if (inputStream == null) return setNull(Types.VARCHAR);
        stmt.setBlob(index(), inputStream, length);
        return this;
    }

    /**
     * Sets the designated parameter to a {@code Reader} object.  The reader must contain  the number
     * of characters specified by length otherwise a {@code SQLException} will be
     * generated when the {@code PreparedStatement} is executed.
     * This method differs from the {@code setCharacterStream (int, Reader, int)} method
     * because it informs the driver that the parameter value should be sent to
     * the server as a {@code NCLOB}.  When the {@code setCharacterStream} method is used, the
     * driver may have to do extra work to determine whether the parameter
     * data should be sent to the server as a {@code LONGNVARCHAR} or a {@code NCLOB}
     *
     * @param reader An object that contains the data to set the parameter value to.
     * @param length the number of characters in the parameter data.
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if the length specified is less than zero;
     *                                         if the driver does not support national character sets;
     *                                         if the driver can detect that a data conversion
     *                                         error could occur;  if a database access error occurs or
     *                                         this method is called on a closed {@code PreparedStatement}
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setNClob(Reader reader, long length) throws SQLException {
        if (reader == null) return setNull(Types.VARCHAR);
        stmt.setNClob(index(), reader, length);
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code java.sql.SQLXML} object.
     * The driver converts this to an
     * SQL {@code XML} value when it sends it to the database.
     *
     * @param xmlObject a {@code SQLXML} object that maps an SQL {@code XML} value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs;
     *                                         this method is called on a closed {@code PreparedStatement}
     *                                         or the {@code java.xml.transform.Result},
     *                                         {@code Writer} or {@code OutputStream} has not been closed for
     *                                         the {@code SQLXML} object
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setSQLXML(SQLXML xmlObject) throws SQLException {
        if (xmlObject == null) return setNull(Types.SQLXML);
        stmt.setSQLXML(index(), xmlObject);
        return this;
    }

    /**
     * <p>Sets the value of the designated parameter with the given object.
     * <p>
     * If the second argument is an {@code InputStream} then the stream must contain
     * the number of bytes specified by scaleOrLength.  If the second argument is a
     * {@code Reader} then the reader must contain the number of characters specified
     * by scaleOrLength. If these conditions are not true the driver will generate a
     * {@code SQLException} when the prepared statement is executed.
     *
     * <p>The given Java object will be converted to the given targetSqlType
     * before being sent to the database.
     * <p>
     * If the object has a custom mapping (is of a class implementing the
     * interface {@code SQLData}),
     * the JDBC driver should call the method {@code SQLData.writeSQL} to
     * write it to the SQL data stream.
     * If, on the other hand, the object is of a class implementing
     * {@code Ref}, {@code Blob}, {@code Clob},  {@code NClob},
     * {@code Struct}, {@code java.net.URL},
     * or {@code Array}, the driver should pass it to the database as a
     * value of the corresponding SQL type.
     *
     * <p>Note that this method may be used to pass database-specific
     * abstract data types.
     *
     * @param x             the object containing the input parameter value
     * @param targetSqlType the SQL type (as defined in java.sql.Types) to be
     *                      sent to the database. The scale argument may further qualify this type.
     * @param scaleOrLength for {@code java.sql.Types.DECIMAL}
     *                      or {@code java.sql.Types.NUMERIC types},
     *                      this is the number of digits after the decimal point. For
     *                      Java Object types {@code InputStream} and {@code Reader},
     *                      this is the length
     *                      of the data in the stream or reader.  For all other types,
     *                      this value will be ignored.
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs;
     *                                         this method is called on a closed {@code PreparedStatement} or
     *                                         if the Java Object specified by x is an InputStream
     *                                         or Reader object and the value of the scale parameter is less
     *                                         than zero
     * @throws SQLFeatureNotSupportedException if
     *                                         the JDBC driver does not support the specified targetSqlType
     * @see Types
     */
    public ParamBuilder setObject(Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        if (x == null) return setNull(targetSqlType);
        stmt.setObject(index(), x, targetSqlType, scaleOrLength);
        return this;
    }

    /**
     * Sets the designated parameter to the given input stream, which will have
     * the specified number of bytes.
     * When a very large ASCII value is input to a {@code LONGVARCHAR}
     * parameter, it may be more practical to send it via a
     * {@code java.io.InputStream}. Data will be read from the stream
     * as needed until end-of-file is reached.  The JDBC driver will
     * do any necessary conversion from ASCII to the database char format.
     *
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     *
     * @param x      the Java input stream that contains the ASCII parameter value
     * @param length the number of bytes in the stream
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     * @since 1.6
     */
    public ParamBuilder setAsciiStream(InputStream x, long length) throws SQLException {
        if (x == null) return setNull(Types.VARCHAR);
        stmt.setAsciiStream(index(), x, length);
        return this;
    }

    /**
     * Sets the designated parameter to the given input stream, which will have
     * the specified number of bytes.
     * When a very large binary value is input to a {@code LONGVARBINARY}
     * parameter, it may be more practical to send it via a
     * {@code java.io.InputStream} object. The data will be read from the
     * stream as needed until end-of-file is reached.
     *
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     *
     * @param x      the java input stream which contains the binary parameter value
     * @param length the number of bytes in the stream
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     * @since 1.6
     */
    public ParamBuilder setBinaryStream(InputStream x, long length) throws SQLException {
        if (x == null) return setNull(Types.LONGVARBINARY);
        stmt.setBinaryStream(index(), x, length);
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code Reader}
     * object, which is the given number of characters long.
     * When a very large UNICODE value is input to a {@code LONGVARCHAR}
     * parameter, it may be more practical to send it via a
     * {@code java.io.Reader} object. The data will be read from the stream
     * as needed until end-of-file is reached.  The JDBC driver will
     * do any necessary conversion from UNICODE to the database char format.
     *
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     *
     * @param reader the {@code java.io.Reader} object that contains the
     *               Unicode data
     * @param length the number of characters in the stream
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed {@code PreparedStatement}
     * @since 1.6
     */
    public ParamBuilder setCharacterStream(Reader reader, long length) throws SQLException {
        if (reader == null) return setNull(Types.LONGVARCHAR);
        stmt.setCharacterStream(index(), reader, length);
        return this;
    }

    /**
     * Sets the designated parameter to the given input stream.
     * When a very large ASCII value is input to a {@code LONGVARCHAR}
     * parameter, it may be more practical to send it via a
     * {@code java.io.InputStream}. Data will be read from the stream
     * as needed until end-of-file is reached.  The JDBC driver will
     * do any necessary conversion from ASCII to the database char format.
     *
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     * <P><B>Note:</B> Consult your JDBC driver documentation to determine if
     * it might be more efficient to use a version of
     * {@code setAsciiStream} which takes a length parameter.
     *
     * @param x the Java input stream that contains the ASCII parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed {@code PreparedStatement}
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setAsciiStream(InputStream x) throws SQLException {
        if (x == null) return setNull(Types.LONGVARCHAR);
        stmt.setAsciiStream(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given input stream.
     * When a very large binary value is input to a {@code LONGVARBINARY}
     * parameter, it may be more practical to send it via a
     * {@code java.io.InputStream} object. The data will be read from the
     * stream as needed until end-of-file is reached.
     *
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     * <P><B>Note:</B> Consult your JDBC driver documentation to determine if
     * it might be more efficient to use a version of
     * {@code setBinaryStream} which takes a length parameter.
     *
     * @param x the java input stream which contains the binary parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed {@code PreparedStatement}
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setBinaryStream(InputStream x) throws SQLException {
        if (x == null) return setNull(Types.LONGVARBINARY);
        stmt.setBinaryStream(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given {@code Reader}
     * object.
     * When a very large UNICODE value is input to a {@code LONGVARCHAR}
     * parameter, it may be more practical to send it via a
     * {@code java.io.Reader} object. The data will be read from the stream
     * as needed until end-of-file is reached.  The JDBC driver will
     * do any necessary conversion from UNICODE to the database char format.
     *
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     * <P><B>Note:</B> Consult your JDBC driver documentation to determine if
     * it might be more efficient to use a version of
     * {@code setCharacterStream} which takes a length parameter.
     *
     * @param reader the {@code java.io.Reader} object that contains the
     *               Unicode data
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed {@code PreparedStatement}
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setCharacterStream(Reader reader) throws SQLException {
        if (reader == null) return setNull(Types.LONGVARCHAR);
        stmt.setCharacterStream(index(), reader);
        return this;
    }

    /**
     * Sets the designated parameter to a {@code Reader} object. The
     * {@code Reader} reads the data till end-of-file is reached. The
     * driver does the necessary conversion from Java character format to
     * the national character set in the database.
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     * <P><B>Note:</B> Consult your JDBC driver documentation to determine if
     * it might be more efficient to use a version of
     * {@code setNCharacterStream} which takes a length parameter.
     *
     * @param value the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if the driver does not support national
     *                                         character sets;  if the driver can detect that a data conversion
     *                                         error could occur; if a database access error occurs; or
     *                                         this method is called on a closed {@code PreparedStatement}
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setNCharacterStream(Reader value) throws SQLException {
        if (value == null) return setNull(Types.VARCHAR);
        stmt.setNCharacterStream(index(), value);
        return this;
    }

    /**
     * Sets the designated parameter to a {@code Reader} object.
     * This method differs from the {@code setCharacterStream (int, Reader)} method
     * because it informs the driver that the parameter value should be sent to
     * the server as a {@code CLOB}.  When the {@code setCharacterStream} method is used, the
     * driver may have to do extra work to determine whether the parameter
     * data should be sent to the server as a {@code LONGVARCHAR} or a {@code CLOB}
     *
     * <P><B>Note:</B> Consult your JDBC driver documentation to determine if
     * it might be more efficient to use a version of
     * {@code setClob} which takes a length parameter.
     *
     * @param reader An object that contains the data to set the parameter value to.
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs; this method is called on
     *                                         a closed {@code PreparedStatement}or if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setClob(Reader reader) throws SQLException {
        if (reader == null) return setNull(Types.CLOB);
        stmt.setClob(index(), reader);
        return this;
    }

    /**
     * Sets the designated parameter to a {@code InputStream} object.
     * This method differs from the {@code setBinaryStream (int, InputStream)}
     * method because it informs the driver that the parameter value should be
     * sent to the server as a {@code BLOB}.  When the {@code setBinaryStream} method is used,
     * the driver may have to do extra work to determine whether the parameter
     * data should be sent to the server as a {@code LONGVARBINARY} or a {@code BLOB}
     *
     * <P><B>Note:</B> Consult your JDBC driver documentation to determine if
     * it might be more efficient to use a version of
     * {@code setBlob} which takes a length parameter.
     *
     * @param inputStream An object that contains the data to set the parameter
     *                    value to.
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs;
     *                                         this method is called on a closed {@code PreparedStatement} or
     *                                         if parameterIndex does not correspond
     *                                         to a parameter marker in the SQL statement,
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setBlob(InputStream inputStream) throws SQLException {
        if (inputStream == null) return setNull(Types.VARCHAR);
        stmt.setBlob(index(), inputStream);
        return this;
    }

    /**
     * Sets the designated parameter to a {@code Reader} object.
     * This method differs from the {@code setCharacterStream (int, Reader)} method
     * because it informs the driver that the parameter value should be sent to
     * the server as a {@code NCLOB}.  When the {@code setCharacterStream} method is used, the
     * driver may have to do extra work to determine whether the parameter
     * data should be sent to the server as a {@code LONGNVARCHAR} or a {@code NCLOB}
     * <P><B>Note:</B> Consult your JDBC driver documentation to determine if
     * it might be more efficient to use a version of
     * {@code setNClob} which takes a length parameter.
     *
     * @param reader An object that contains the data to set the parameter value to.
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement;
     *                                         if the driver does not support national character sets;
     *                                         if the driver can detect that a data conversion
     *                                         error could occur;  if a database access error occurs or
     *                                         this method is called on a closed {@code PreparedStatement}
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setNClob(Reader reader) throws SQLException {
        if (reader == null) return setNull(Types.VARCHAR);
        stmt.setNClob(index(), reader);
        return this;
    }

    /**
     * Sets the value of the designated parameter with the given object.
     * <p>
     * This method is similar to {@link #setObject(Object, int, int)},
     * except that it assumes a scale of zero.
     * <p>
     * The default implementation will throw {@code SQLFeatureNotSupportedException}
     *
     * @param x             the object containing the input parameter value
     * @param targetSqlType the SQL type to be sent to the database
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a
     *                                         parameter marker in the SQL statement; if a database access error occurs
     *                                         or this method is called on a closed {@code PreparedStatement}
     * @throws SQLFeatureNotSupportedException if
     *                                         the JDBC driver does not support the specified targetSqlType
     * @see JDBCType
     * @see SQLType
     * @since 1.8
     */
    public ParamBuilder setObject(Object x, SQLType targetSqlType) throws SQLException {
        if (x == null) return setNull(targetSqlType.getVendorTypeNumber());
        stmt.setObject(index(), x, targetSqlType);
        return this;
    }
}
