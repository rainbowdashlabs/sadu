/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper.wrapper;

import de.chojo.sadu.core.conversion.ArrayConverter;
import de.chojo.sadu.core.conversion.UUIDConverter;
import de.chojo.sadu.mapper.MapperConfig;
import org.jetbrains.annotations.ApiStatus;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represents the row of a result set. Made to restrict actions on valid calls.
 */
@SuppressWarnings({"unused", "UseOfObsoleteDateTimeApi"})
public class Row {
    private final ResultSet resultSet;

    private final MapperConfig config;

    /**
     * Wraps a result set into a row
     *
     * @param resultSet result set
     */
    public Row(ResultSet resultSet, MapperConfig config) {
        this.resultSet = resultSet;
        this.config = config;
    }

    /**
     * Get the underlying result set.
     * <p>
     * <b>This result set should never be modified by moving the cursor</b>
     *
     * @return resultSet
     */
    @ApiStatus.Internal
    public ResultSet resultSet() {
        return resultSet;
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code String} in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public String getString(int columnIndex) throws SQLException {
        return resultSet.getString(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code String} in the Java programming language and maps it to an enum.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param clazz       the enum clazz
     * @param <T>         type of enum
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException             if the columnIndex is not valid;
     *                                  if a database access error occurs or this method is
     *                                  called on a closed result set
     * @throws IllegalArgumentException when the value could not be mapped
     * @throws NullPointerException     when the class is null
     * @since 1.2
     */
    public <T extends Enum<T>> T getEnum(int columnIndex, Class<T> clazz) throws SQLException {
        var value = getString(columnIndex);
        if (value == null) {
            return null;
        }
        return Enum.valueOf(clazz, value);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code UUID} in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException             if the columnIndex is not valid;
     *                                  if a database access error occurs or this method is
     *                                  called on a closed result set
     * @throws IllegalArgumentException If value does not conform to the string representation as
     *                                  described in {@link #toString}
     */
    public UUID getUuidFromString(int columnIndex) throws SQLException {
        return UUID.fromString(resultSet.getString(columnIndex));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code boolean} in the Java programming language.
     *
     * <P>If the designated column has a datatype of CHAR or VARCHAR
     * and contains a "0" or has a datatype of BIT, TINYINT, SMALLINT, INTEGER or BIGINT
     * and contains  a 0, a value of {@code false} is returned.  If the designated column has a datatype
     * of CHAR or VARCHAR
     * and contains a "1" or has a datatype of BIT, TINYINT, SMALLINT, INTEGER or BIGINT
     * and contains  a 1, a value of {@code true} is returned.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code false}
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public boolean getBoolean(int columnIndex) throws SQLException {
        return resultSet.getBoolean(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code byte} in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code 0}
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public byte getByte(int columnIndex) throws SQLException {
        return resultSet.getByte(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code short} in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code 0}
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public short getShort(int columnIndex) throws SQLException {
        return resultSet.getShort(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * an {@code int} in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code 0}
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public int getInt(int columnIndex) throws SQLException {
        return resultSet.getInt(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code long} in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code 0}
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public long getLong(int columnIndex) throws SQLException {
        return resultSet.getLong(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code float} in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code 0}
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public float getFloat(int columnIndex) throws SQLException {
        return resultSet.getFloat(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code double} in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code 0}
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public double getDouble(int columnIndex) throws SQLException {
        return resultSet.getDouble(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code byte} array in the Java programming language.
     * The bytes represent the raw values returned by the driver.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public byte[] getBytes(int columnIndex) throws SQLException {
        return resultSet.getBytes(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code UUID} in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public UUID getUuidFromBytes(int columnIndex) throws SQLException {
        return UUIDConverter.convert(resultSet.getBytes(columnIndex));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code java.sql.Date} object in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public Date getDate(int columnIndex) throws SQLException {
        return resultSet.getDate(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code java.time.LocalDateTime} object in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public LocalDate getLocalDate(int columnIndex) throws SQLException {
        var date = getDate(columnIndex);
        return date == null ? null : date.toLocalDate();
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code java.sql.Time} object in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public Time getTime(int columnIndex) throws SQLException {
        return resultSet.getTime(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code java.time.LocalTime} object in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public LocalTime getLocalTime(int columnIndex) throws SQLException {
        var time = getTime(columnIndex);
        return time != null ? time.toLocalTime() : null;
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code java.sql.Timestamp} object in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return resultSet.getTimestamp(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code java.time.LocalDateTime} object in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public LocalDateTime getLocalDateTime(int columnIndex) throws SQLException {
        Timestamp timestamp = getTimestamp(columnIndex);
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code java.time.ZonedDateTime} object in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public ZonedDateTime getZonedDateTime(int columnIndex) throws SQLException {
        LocalDateTime localDateTime = getLocalDateTime(columnIndex);
        return localDateTime == null ? null : ZonedDateTime.from(localDateTime);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code java.time.OffsetDateTime} object in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public OffsetDateTime getOffsetDateTime(int columnIndex) throws SQLException {
        LocalDateTime localDateTime = getLocalDateTime(columnIndex);
        return localDateTime == null ? null : OffsetDateTime.from(localDateTime);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code java.time.OffsetTime} object in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public OffsetTime getOffsetTime(int columnIndex) throws SQLException {
        LocalTime localDateTime = getLocalTime(columnIndex);
        return localDateTime == null ? null : OffsetTime.from(localDateTime);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a stream of ASCII characters. The value can then be read in chunks from the
     * stream. This method is particularly
     * suitable for retrieving large {@code LONGVARCHAR} values.
     * The JDBC driver will
     * do any necessary conversion from the database format into ASCII.
     *
     * <P><B>Note:</B> All the data in the returned stream must be
     * read prior to getting the value of any other column. The next
     * call to a getter method implicitly closes the stream.  Also, a
     * stream may return {@code 0} when the method
     * {@code InputStream.available}
     * is called whether there is data available or not.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return a Java input stream that delivers the database column value
     * as a stream of one-byte ASCII characters;
     * if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return resultSet.getAsciiStream(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a  stream of
     * uninterpreted bytes. The value can then be read in chunks from the
     * stream. This method is particularly
     * suitable for retrieving large {@code LONGVARBINARY} values.
     *
     * <P><B>Note:</B> All the data in the returned stream must be
     * read prior to getting the value of any other column. The next
     * call to a getter method implicitly closes the stream.  Also, a
     * stream may return {@code 0} when the method
     * {@code InputStream.available}
     * is called whether there is data available or not.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return a Java input stream that delivers the database column value
     * as a stream of uninterpreted bytes;
     * if the value is SQL {@code NULL}, the value returned is
     * {@code null}
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return resultSet.getBinaryStream(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code String} in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public String getString(String columnLabel) throws SQLException {
        return resultSet.getString(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code String} in the Java programming language and maps it to an enum.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @param clazz       the enum clazz
     * @param <T>         type of enum
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException             if the columnIndex is not valid;
     *                                  if a database access error occurs or this method is
     *                                  called on a closed result set
     * @throws IllegalArgumentException when the value could not be mapped
     * @throws NullPointerException     when the class is null
     * @since 1.2
     */
    public <T extends Enum<T>> T getEnum(String columnLabel, Class<T> clazz) throws SQLException {
        var value = getString(columnLabel);
        if (value == null) {
            return null;
        }
        return Enum.valueOf(clazz, value);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code UUID} in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public UUID getUuidFromString(String columnLabel) throws SQLException {
        return UUID.fromString(resultSet.getString(columnAlias(columnLabel)));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code boolean} in the Java programming language.
     *
     * <P>If the designated column has a datatype of CHAR or VARCHAR
     * and contains a "0" or has a datatype of BIT, TINYINT, SMALLINT, INTEGER or BIGINT
     * and contains  a 0, a value of {@code false} is returned.  If the designated column has a datatype
     * of CHAR or VARCHAR
     * and contains a "1" or has a datatype of BIT, TINYINT, SMALLINT, INTEGER or BIGINT
     * and contains  a 1, a value of {@code true} is returned.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code false}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public boolean getBoolean(String columnLabel) throws SQLException {
        return resultSet.getBoolean(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code byte} in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code 0}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public byte getByte(String columnLabel) throws SQLException {
        return resultSet.getByte(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code short} in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code 0}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public short getShort(String columnLabel) throws SQLException {
        return resultSet.getShort(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * an {@code int} in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code 0}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public int getInt(String columnLabel) throws SQLException {
        return resultSet.getInt(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code long} in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code 0}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public long getLong(String columnLabel) throws SQLException {
        return resultSet.getLong(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code float} in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code 0}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public float getFloat(String columnLabel) throws SQLException {
        return resultSet.getFloat(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code double} in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code 0}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public double getDouble(String columnLabel) throws SQLException {
        return resultSet.getDouble(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code byte} array in the Java programming language.
     * The bytes represent the raw values returned by the driver.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public byte[] getBytes(String columnLabel) throws SQLException {
        return resultSet.getBytes(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code UUID} in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public UUID getUuidFromBytes(String columnLabel) throws SQLException {
        return UUIDConverter.convert(resultSet.getBytes(columnAlias(columnLabel)));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code UUID} in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @param <T>         type of list
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public <T> List<T> getList(String columnLabel) throws SQLException {
        return ArrayConverter.toList(resultSet, columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code List} in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param <T>         type of list
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public <T> List<T> getList(int columnIndex) throws SQLException {
        return ArrayConverter.toList(resultSet, columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code java.sql.Date} object in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public Date getDate(String columnLabel) throws SQLException {
        return resultSet.getDate(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code java.time.LocalDate} object in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public LocalDate getLocalDate(String columnLabel) throws SQLException {
        var date = getDate(columnLabel);
        return date == null ? null : date.toLocalDate();
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code java.sql.Time} object in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public Time getTime(String columnLabel) throws SQLException {
        return resultSet.getTime(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code java.time.LocalTime} object in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public LocalTime getLocalTime(String columnLabel) throws SQLException {
        var time = getTime(columnLabel);
        return time != null ? time.toLocalTime() : null;
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code java.sql.Timestamp} object in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        return resultSet.getTimestamp(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code java.time.LocalDateTime} object in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public LocalDateTime getLocalDateTime(String columnLabel) throws SQLException {
        Timestamp timestamp = getTimestamp(columnLabel);
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code java.time.ZonedDateTime} object in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public ZonedDateTime getZonedDateTime(String columnLabel) throws SQLException {
        LocalDateTime localDateTime = getLocalDateTime(columnLabel);
        return localDateTime == null ? null : ZonedDateTime.from(localDateTime);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code java.time.OffsetDateTime} object in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public OffsetDateTime getOffsetDateTime(String columnLabel) throws SQLException {
        LocalDateTime localDateTime = getLocalDateTime(columnLabel);
        return localDateTime == null ? null : OffsetDateTime.from(localDateTime);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code java.time.OffsetTime} object in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public OffsetTime getOffsetTime(String columnLabel) throws SQLException {
        LocalTime localDateTime = getLocalTime(columnLabel);
        return localDateTime == null ? null : OffsetTime.from(localDateTime);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a stream of
     * ASCII characters. The value can then be read in chunks from the
     * stream. This method is particularly
     * suitable for retrieving large {@code LONGVARCHAR} values.
     * The JDBC driver will
     * do any necessary conversion from the database format into ASCII.
     *
     * <P><B>Note:</B> All the data in the returned stream must be
     * read prior to getting the value of any other column. The next
     * call to a getter method implicitly closes the stream. Also, a
     * stream may return {@code 0} when the method {@code available}
     * is called whether there is data available or not.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return a Java input stream that delivers the database column value
     * as a stream of one-byte ASCII characters.
     * If the value is SQL {@code NULL},
     * the value returned is {@code null}.
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        return resultSet.getAsciiStream(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a stream of uninterpreted
     * {@code byte}s.
     * The value can then be read in chunks from the
     * stream. This method is particularly
     * suitable for retrieving large {@code LONGVARBINARY}
     * values.
     *
     * <P><B>Note:</B> All the data in the returned stream must be
     * read prior to getting the value of any other column. The next
     * call to a getter method implicitly closes the stream. Also, a
     * stream may return {@code 0} when the method {@code available}
     * is called whether there is data available or not.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return a Java input stream that delivers the database column value
     * as a stream of uninterpreted bytes;
     * if the value is SQL {@code NULL}, the result is {@code null}
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        return resultSet.getBinaryStream(columnAlias(columnLabel));
    }

    /**
     * Retrieves the  number, types and properties of
     * this {@code Row} object's columns.
     *
     * @return the description of this {@code Row} object's columns
     * @throws SQLException if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public ResultSetMetaData getMetaData() throws SQLException {
        return resultSet.getMetaData();
    }

    /**
     * <p>Gets the value of the designated column in the current row
     * of this {@code Row} object as
     * an {@code Object} in the Java programming language.
     *
     * <p>This method will return the value of the given column as a
     * Java object.  The type of the Java object will be the default
     * Java object type corresponding to the column's SQL type,
     * following the mapping for built-in types specified in the JDBC
     * specification. If the value is an SQL {@code NULL},
     * the driver returns a Java {@code null}.
     *
     * <p>This method may also be used to read database-specific
     * abstract data types.
     * <p>
     * In the JDBC 2.0 API, the behavior of method
     * {@code getObject} is extended to materialize
     * data of SQL user-defined types.
     * <p>
     * If {@code Connection.getTypeMap} does not throw a
     * {@code SQLFeatureNotSupportedException},
     * then when a column contains a structured or distinct value,
     * the behavior of this method is as
     * if it were a call to: {@code getObject(columnIndex,
     * this.getStatement().getConnection().getTypeMap())}.
     * <p>
     * If {@code Connection.getTypeMap} does throw a
     * {@code SQLFeatureNotSupportedException},
     * then structured values are not supported, and distinct values
     * are mapped to the default Java class as determined by the
     * underlying SQL type of the DISTINCT type.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return a {@code java.lang.Object} holding the column value
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public Object getObject(int columnIndex) throws SQLException {
        return resultSet.getObject(columnIndex);
    }

    /**
     * <p>Gets the value of the designated column in the current row
     * of this {@code Row} object as
     * an {@code Object} in the Java programming language.
     *
     * <p>This method will return the value of the given column as a
     * Java object.  The type of the Java object will be the default
     * Java object type corresponding to the column's SQL type,
     * following the mapping for built-in types specified in the JDBC
     * specification. If the value is an SQL {@code NULL},
     * the driver returns a Java {@code null}.
     * <p>
     * This method may also be used to read database-specific
     * abstract data types.
     * <p>
     * In the JDBC 2.0 API, the behavior of the method
     * {@code getObject} is extended to materialize
     * data of SQL user-defined types.  When a column contains
     * a structured or distinct value, the behavior of this method is as
     * if it were a call to: {@code getObject(columnIndex,
     * this.getStatement().getConnection().getTypeMap())}.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return a {@code java.lang.Object} holding the column value
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public Object getObject(String columnLabel) throws SQLException {
        return resultSet.getObject(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a
     * {@code java.io.Reader} object.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return a {@code java.io.Reader} object that contains the column
     * value; if the value is SQL {@code NULL}, the value returned is
     * {@code null} in the Java programming language.
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        return resultSet.getCharacterStream(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a
     * {@code java.io.Reader} object.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return a {@code java.io.Reader} object that contains the column
     * value; if the value is SQL {@code NULL}, the value returned is
     * {@code null} in the Java programming language
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public Reader getCharacterStream(String columnLabel) throws SQLException {
        return resultSet.getCharacterStream(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a
     * {@code java.math.BigDecimal} with full precision.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value (full precision);
     * if the value is SQL {@code NULL}, the value returned is
     * {@code null} in the Java programming language.
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return resultSet.getBigDecimal(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a
     * {@code java.math.BigDecimal} with full precision.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value (full precision);
     * if the value is SQL {@code NULL}, the value returned is
     * {@code null} in the Java programming language.
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs or this method is
     *                      called on a closed result set
     */
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return resultSet.getBigDecimal(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as an {@code Object}
     * in the Java programming language.
     * If the value is an SQL {@code NULL},
     * the driver returns a Java {@code null}.
     * This method uses the given {@code Map} object
     * for the custom mapping of the
     * SQL structured or distinct type that is being retrieved.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param map         a {@code java.util.Map} object that contains the mapping
     *                    from SQL type names to classes in the Java programming language
     * @return an {@code Object} in the Java programming language
     * representing the SQL value
     * @throws SQLException                    if the columnIndex is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
        return resultSet.getObject(columnIndex, map);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code Ref} object
     * in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return a {@code Ref} object representing an SQL {@code REF}
     * value
     * @throws SQLException                    if the columnIndex is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public Ref getRef(int columnIndex) throws SQLException {
        return resultSet.getRef(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code Blob} object
     * in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return a {@code Blob} object representing the SQL
     * {@code BLOB} value in the specified column
     * @throws SQLException                    if the columnIndex is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public Blob getBlob(int columnIndex) throws SQLException {
        return resultSet.getBlob(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code Clob} object
     * in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return a {@code Clob} object representing the SQL
     * {@code CLOB} value in the specified column
     * @throws SQLException                    if the columnIndex is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public Clob getClob(int columnIndex) throws SQLException {
        return resultSet.getClob(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as an {@code Array} object
     * in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return an {@code Array} object representing the SQL
     * {@code ARRAY} value in the specified column
     * @throws SQLException                    if the columnIndex is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public Array getArray(int columnIndex) throws SQLException {
        return resultSet.getArray(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as an {@code Object}
     * in the Java programming language.
     * If the value is an SQL {@code NULL},
     * the driver returns a Java {@code null}.
     * This method uses the specified {@code Map} object for
     * custom mapping if appropriate.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @param map         a {@code java.util.Map} object that contains the mapping
     *                    from SQL type names to classes in the Java programming language
     * @return an {@code Object} representing the SQL value in the
     * specified column
     * @throws SQLException                    if the columnLabel is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        return resultSet.getObject(columnAlias(columnLabel), map);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code Ref} object
     * in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return a {@code Ref} object representing the SQL {@code REF}
     * value in the specified column
     * @throws SQLException                    if the columnLabel is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public Ref getRef(String columnLabel) throws SQLException {
        return resultSet.getRef(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code Blob} object
     * in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return a {@code Blob} object representing the SQL {@code BLOB}
     * value in the specified column
     * @throws SQLException                    if the columnLabel is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public Blob getBlob(String columnLabel) throws SQLException {
        return resultSet.getBlob(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code Clob} object
     * in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return a {@code Clob} object representing the SQL {@code CLOB}
     * value in the specified column
     * @throws SQLException                    if the columnLabel is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public Clob getClob(String columnLabel) throws SQLException {
        return resultSet.getClob(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as an {@code Array} object
     * in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return an {@code Array} object representing the SQL {@code ARRAY} value in
     * the specified column
     * @throws SQLException                    if the columnLabel is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public Array getArray(String columnLabel) throws SQLException {
        return resultSet.getArray(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code java.sql.Date} object
     * in the Java programming language.
     * This method uses the given calendar to construct an appropriate millisecond
     * value for the date if the underlying database does not store
     * timezone information.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param cal         the {@code java.util.Calendar} object
     *                    to use in constructing the date
     * @return the column value as a {@code java.sql.Date} object;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null} in the Java programming language
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs
     *                      or this method is called on a closed result set
     */
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return resultSet.getDate(columnIndex, cal);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code java.time.LocalDate} object
     * in the Java programming language.
     * This method uses the given calendar to construct an appropriate millisecond
     * value for the date if the underlying database does not store
     * timezone information.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param cal         the {@code java.util.Calendar} object
     *                    to use in constructing the date
     * @return the column value as a {@code java.sql.Date} object;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null} in the Java programming language
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs
     *                      or this method is called on a closed result set
     */
    public LocalDate getLocalDate(int columnIndex, Calendar cal) throws SQLException {
        var date = getDate(columnIndex, cal);
        return date == null ? null : date.toLocalDate();
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code java.sql.Date} object
     * in the Java programming language.
     * This method uses the given calendar to construct an appropriate millisecond
     * value for the date if the underlying database does not store
     * timezone information.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @param cal         the {@code java.util.Calendar} object
     *                    to use in constructing the date
     * @return the column value as a {@code java.sql.Date} object;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null} in the Java programming language
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or this method is called on a closed result set
     */
    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        return resultSet.getDate(columnAlias(columnLabel), cal);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code java.time.LocalDate} object
     * in the Java programming language.
     * This method uses the given calendar to construct an appropriate millisecond
     * value for the date if the underlying database does not store
     * timezone information.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @param cal         the {@code java.util.Calendar} object
     *                    to use in constructing the date
     * @return the column value as a {@code java.sql.Date} object;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null} in the Java programming language
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or this method is called on a closed result set
     */
    public LocalDate getLocalDate(String columnLabel, Calendar cal) throws SQLException {
        var date = getDate(columnLabel, cal);
        return date == null ? null : date.toLocalDate();
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code java.sql.Time} object
     * in the Java programming language.
     * This method uses the given calendar to construct an appropriate millisecond
     * value for the time if the underlying database does not store
     * timezone information.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param cal         the {@code java.util.Calendar} object
     *                    to use in constructing the time
     * @return the column value as a {@code java.sql.Time} object;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null} in the Java programming language
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs
     *                      or this method is called on a closed result set
     */
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return resultSet.getTime(columnIndex, cal);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code java.time.LocalTime} object
     * in the Java programming language.
     * This method uses the given calendar to construct an appropriate millisecond
     * value for the time if the underlying database does not store
     * timezone information.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param cal         the {@code java.util.Calendar} object
     *                    to use in constructing the time
     * @return the column value as a {@code java.sql.Time} object;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null} in the Java programming language
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs
     *                      or this method is called on a closed result set
     */
    public LocalTime getLocalTime(int columnIndex, Calendar cal) throws SQLException {
        var time = getTime(columnIndex, cal);
        return time != null ? time.toLocalTime() : null;
    }


    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code java.sql.Time} object
     * in the Java programming language.
     * This method uses the given calendar to construct an appropriate millisecond
     * value for the time if the underlying database does not store
     * timezone information.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @param cal         the {@code java.util.Calendar} object
     *                    to use in constructing the time
     * @return the column value as a {@code java.sql.Time} object;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null} in the Java programming language
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or this method is called on a closed result set
     */
    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        return resultSet.getTime(columnAlias(columnLabel), cal);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code java.time.LocalTime} object
     * in the Java programming language.
     * This method uses the given calendar to construct an appropriate millisecond
     * value for the time if the underlying database does not store
     * timezone information.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @param cal         the {@code java.util.Calendar} object
     *                    to use in constructing the time
     * @return the column value as a {@code java.sql.Time} object;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null} in the Java programming language
     * @throws SQLException if the columnLabel is not valid;
     *                      if a database access error occurs
     *                      or this method is called on a closed result set
     */
    public LocalTime getLocalTime(String columnLabel, Calendar cal) throws SQLException {
        var time = getTime(columnLabel, cal);
        return time != null ? time.toLocalTime() : null;
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code java.sql.Timestamp} object
     * in the Java programming language.
     * This method uses the given calendar to construct an appropriate millisecond
     * value for the timestamp if the underlying database does not store
     * timezone information.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param cal         the {@code java.util.Calendar} object
     *                    to use in constructing the timestamp
     * @return the column value as a {@code java.sql.Timestamp} object;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null} in the Java programming language
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs
     *                      or this method is called on a closed result set
     */
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        return resultSet.getTimestamp(columnIndex, cal);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code java.sql.Timestamp} object
     * in the Java programming language.
     * This method uses the given calendar to construct an appropriate millisecond
     * value for the timestamp if the underlying database does not store
     * timezone information.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @param cal         the {@code java.util.Calendar} object
     *                    to use in constructing the date
     * @return the column value as a {@code java.sql.Timestamp} object;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null} in the Java programming language
     * @throws SQLException if the columnLabel is not valid or
     *                      if a database access error occurs
     *                      or this method is called on a closed result set
     */
    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        return resultSet.getTimestamp(columnAlias(columnLabel), cal);
    }


    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code java.time.LocalDateTime} object
     * in the Java programming language.
     * This method uses the given calendar to construct an appropriate millisecond
     * value for the timestamp if the underlying database does not store
     * timezone information.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param cal         the {@code java.util.Calendar} object
     *                    to use in constructing the timestamp
     * @return the column value as a {@code java.time.LocalDateTime} object;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null} in the Java programming language
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs
     *                      or this method is called on a closed result set
     */
    public LocalDateTime getLocalDateTime(int columnIndex, Calendar cal) throws SQLException {
        Timestamp timestamp = getTimestamp(columnIndex, cal);
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }


    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code java.time.LocalDateTime} object
     * in the Java programming language.
     * This method uses the given calendar to construct an appropriate millisecond
     * value for the timestamp if the underlying database does not store
     * timezone information.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @param cal         the {@code java.util.Calendar} object
     *                    to use in constructing the date
     * @return the column value as a {@code java.time.LocalDateTime} object;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null} in the Java programming language
     * @throws SQLException if the columnLabel is not valid or
     *                      if a database access error occurs
     *                      or this method is called on a closed result set
     */
    public LocalDateTime getLocalDateTime(String columnLabel, Calendar cal) throws SQLException {
        Timestamp timestamp = getTimestamp(columnLabel, cal);
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code java.time.ZonedDateTime} object
     * in the Java programming language.
     * This method uses the given calendar to construct an appropriate millisecond
     * value for the timestamp if the underlying database does not store
     * timezone information.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param cal         the {@code java.util.Calendar} object
     *                    to use in constructing the timestamp
     * @return the column value as a {@code java.time.LocalDateTime} object;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null} in the Java programming language
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs
     *                      or this method is called on a closed result set
     */
    public ZonedDateTime getZonedDateTime(int columnIndex, Calendar cal) throws SQLException {
        LocalDateTime localDateTime = getLocalDateTime(columnIndex, cal);
        return localDateTime == null ? null : ZonedDateTime.from(localDateTime);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code java.time.ZonedDateTime} object
     * in the Java programming language.
     * This method uses the given calendar to construct an appropriate millisecond
     * value for the timestamp if the underlying database does not store
     * timezone information.
     *
     * @param columnLabel columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @param cal         the {@code java.util.Calendar} object
     *                    to use in constructing the timestamp
     * @return the column value as a {@code java.time.LocalDateTime} object;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null} in the Java programming language
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs
     *                      or this method is called on a closed result set
     */
    public ZonedDateTime getZonedDateTime(String columnLabel, Calendar cal) throws SQLException {
        LocalDateTime localDateTime = getLocalDateTime(columnLabel, cal);
        return localDateTime == null ? null : ZonedDateTime.from(localDateTime);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code java.time.OffsetDateTime} object
     * in the Java programming language.
     * This method uses the given calendar to construct an appropriate millisecond
     * value for the timestamp if the underlying database does not store
     * timezone information.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param cal         the {@code java.util.Calendar} object
     *                    to use in constructing the timestamp
     * @return the column value as a {@code java.time.LocalDateTime} object;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null} in the Java programming language
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs
     *                      or this method is called on a closed result set
     */
    public OffsetDateTime getOffsetDateTime(int columnIndex, Calendar cal) throws SQLException {
        LocalDateTime localDateTime = getLocalDateTime(columnIndex, cal);
        return localDateTime == null ? null : OffsetDateTime.from(localDateTime);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code java.time.OffsetDateTime} object
     * in the Java programming language.
     * This method uses the given calendar to construct an appropriate millisecond
     * value for the timestamp if the underlying database does not store
     * timezone information.
     *
     * @param columnLabel columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @param cal         the {@code java.util.Calendar} object
     *                    to use in constructing the timestamp
     * @return the column value as a {@code java.time.LocalDateTime} object;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null} in the Java programming language
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs
     *                      or this method is called on a closed result set
     */
    public OffsetDateTime getOffsetDateTime(String columnLabel, Calendar cal) throws SQLException {
        LocalDateTime localDateTime = getLocalDateTime(columnLabel, cal);
        return localDateTime == null ? null : OffsetDateTime.from(localDateTime);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code java.time.OffsetTime} object
     * in the Java programming language.
     * This method uses the given calendar to construct an appropriate millisecond
     * value for the timestamp if the underlying database does not store
     * timezone information.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param cal         the {@code java.util.Calendar} object
     *                    to use in constructing the timestamp
     * @return the column value as a {@code java.time.LocalDateTime} object;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null} in the Java programming language
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs
     *                      or this method is called on a closed result set
     */
    public OffsetTime getOffsetTime(int columnIndex, Calendar cal) throws SQLException {
        LocalTime localDateTime = getLocalTime(columnIndex, cal);
        return localDateTime == null ? null : OffsetTime.from(localDateTime);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code java.time.OffsetTime} object
     * in the Java programming language.
     * This method uses the given calendar to construct an appropriate millisecond
     * value for the timestamp if the underlying database does not store
     * timezone information.
     *
     * @param columnLabel columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @param cal         the {@code java.util.Calendar} object
     *                    to use in constructing the timestamp
     * @return the column value as a {@code java.time.LocalDateTime} object;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null} in the Java programming language
     * @throws SQLException if the columnIndex is not valid;
     *                      if a database access error occurs
     *                      or this method is called on a closed result set
     */
    public OffsetTime getOffsetTime(String columnLabel, Calendar cal) throws SQLException {
        LocalTime localDateTime = getLocalTime(columnLabel, cal);
        return localDateTime == null ? null : OffsetTime.from(localDateTime);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code java.net.URL}
     * object in the Java programming language.
     *
     * @param columnIndex the index of the column 1 is the first, 2 is the second,...
     * @return the column value as a {@code java.net.URL} object;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null} in the Java programming language
     * @throws SQLException                    if the columnIndex is not valid;
     *                                         if a database access error occurs; this method
     *                                         is called on a closed result set or if a URL is malformed
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public URL getURL(int columnIndex) throws SQLException {
        return resultSet.getURL(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code java.net.URL}
     * object in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value as a {@code java.net.URL} object;
     * if the value is SQL {@code NULL},
     * the value returned is {@code null} in the Java programming language
     * @throws SQLException                    if the columnLabel is not valid;
     *                                         if a database access error occurs; this method
     *                                         is called on a closed result set or if a URL is malformed
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public URL getURL(String columnLabel) throws SQLException {
        return resultSet.getURL(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * {@code Row} object as a {@code java.sql.RowId} object in the Java
     * programming language.
     *
     * @param columnIndex the first column is 1, the second 2, ...
     * @return the column value; if the value is a SQL {@code NULL} the
     * value returned is {@code null}
     * @throws SQLException                    if the columnIndex is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public RowId getRowId(int columnIndex) throws SQLException {
        return resultSet.getRowId(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * {@code Row} object as a {@code java.sql.RowId} object in the Java
     * programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value ; if the value is a SQL {@code NULL} the
     * value returned is {@code null}
     * @throws SQLException                    if the columnLabel is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public RowId getRowId(String columnLabel) throws SQLException {
        return resultSet.getRowId(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code NClob} object
     * in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return a {@code NClob} object representing the SQL
     * {@code NCLOB} value in the specified column
     * @throws SQLException                    if the columnIndex is not valid;
     *                                         if the driver does not support national
     *                                         character sets;  if the driver can detect that a data conversion
     *                                         error could occur; this method is called on a closed result set
     *                                         or if a database access error occurs
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public NClob getNClob(int columnIndex) throws SQLException {
        return resultSet.getNClob(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a {@code NClob} object
     * in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return a {@code NClob} object representing the SQL {@code NCLOB}
     * value in the specified column
     * @throws SQLException                    if the columnLabel is not valid;
     *                                         if the driver does not support national
     *                                         character sets;  if the driver can detect that a data conversion
     *                                         error could occur; this method is called on a closed result set
     *                                         or if a database access error occurs
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public NClob getNClob(String columnLabel) throws SQLException {
        return resultSet.getNClob(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in  the current row of
     * this {@code Row} as a
     * {@code java.sql.SQLXML} object in the Java programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return a {@code SQLXML} object that maps an {@code SQL XML} value
     * @throws SQLException                    if the columnIndex is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        return resultSet.getSQLXML(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in  the current row of
     * this {@code Row} as a
     * {@code java.sql.SQLXML} object in the Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return a {@code SQLXML} object that maps an {@code SQL XML} value
     * @throws SQLException                    if the columnLabel is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        return resultSet.getSQLXML(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code String} in the Java programming language.
     * It is intended for use when
     * accessing  {@code NCHAR},{@code NVARCHAR}
     * and {@code LONGNVARCHAR} columns.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException                    if the columnIndex is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public String getNString(int columnIndex) throws SQLException {
        return resultSet.getNString(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as
     * a {@code String} in the Java programming language.
     * It is intended for use when
     * accessing  {@code NCHAR},{@code NVARCHAR}
     * and {@code LONGNVARCHAR} columns.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return the column value; if the value is SQL {@code NULL}, the
     * value returned is {@code null}
     * @throws SQLException                    if the columnLabel is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public String getNString(String columnLabel) throws SQLException {
        return resultSet.getNString(columnAlias(columnLabel));
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a
     * {@code java.io.Reader} object.
     * It is intended for use when
     * accessing  {@code NCHAR},{@code NVARCHAR}
     * and {@code LONGNVARCHAR} columns.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return a {@code java.io.Reader} object that contains the column
     * value; if the value is SQL {@code NULL}, the value returned is
     * {@code null} in the Java programming language.
     * @throws SQLException                    if the columnIndex is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        return resultSet.getNCharacterStream(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code Row} object as a
     * {@code java.io.Reader} object.
     * It is intended for use when
     * accessing  {@code NCHAR},{@code NVARCHAR}
     * and {@code LONGNVARCHAR} columns.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS clause was not specified, then the label is the name of the column
     * @return a {@code java.io.Reader} object that contains the column
     * value; if the value is SQL {@code NULL}, the value returned is
     * {@code null} in the Java programming language
     * @throws SQLException                    if the columnLabel is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        return resultSet.getNCharacterStream(columnAlias(columnLabel));
    }

    /**
     * <p>Retrieves the value of the designated column in the current row
     * of this {@code Row} object and will convert from the
     * SQL type of the column to the requested Java data type, if the
     * conversion is supported. If the conversion is not
     * supported  or null is specified for the type, a
     * {@code SQLException} is thrown.
     * <p>
     * At a minimum, an implementation must support the conversions defined in
     * Appendix B, Table B-3 and conversion of appropriate user defined SQL
     * types to a Java type which implements {@code SQLData}, or {@code Struct}.
     * Additional conversions may be supported and are vendor defined.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param type        Class representing the Java data type to convert the designated
     *                    column to.
     * @param <T>         Type of object
     * @return an instance of {@code type} holding the column value
     * @throws SQLException                    if conversion is not supported, type is null or
     *                                         another error occurs. The getCause() method of the
     *                                         exception may provide a more detailed exception, for example, if
     *                                         a conversion error occurs
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        return resultSet.getObject(columnIndex, type);
    }

    /**
     * <p>Retrieves the value of the designated column in the current row
     * of this {@code Row} object and will convert from the
     * SQL type of the column to the requested Java data type, if the
     * conversion is supported. If the conversion is not
     * supported  or null is specified for the type, a
     * {@code SQLException} is thrown.
     * <p>
     * At a minimum, an implementation must support the conversions defined in
     * Appendix B, Table B-3 and conversion of appropriate user defined SQL
     * types to a Java type which implements {@code SQLData}, or {@code Struct}.
     * Additional conversions may be supported and are vendor defined.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     *                    If the SQL AS clause was not specified, then the label is the name
     *                    of the column
     * @param type        Class representing the Java data type to convert the designated
     *                    column to.
     * @param <T>         Type of object
     * @return an instance of {@code type} holding the column value
     * @throws SQLException                    if conversion is not supported, type is null or
     *                                         another error occurs. The getCause() method of the
     *                                         exception may provide a more detailed exception, for example, if
     *                                         a conversion error occurs
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        return resultSet.getObject(columnAlias(columnLabel), type);
    }

    private String columnAlias(String columnLabel) {
        return config.aliases().getOrDefault(columnLabel, columnLabel);
    }
}
