/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper;

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

/**
 * A class which wrapps a {@link PreparedStatement} and allows to set the values with a builder pattern.
 * <p>
 * The index of the argument will be moved automatically
 */
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
     * Sets the designated parameter to SQL <code>NULL</code>.
     *
     * <P><B>Note:</B> You must specify the parameter's SQL type.
     *
     * @param sqlType the SQL type code defined in <code>java.sql.Types</code>
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed <code>PreparedStatement</code>
     * @throws SQLFeatureNotSupportedException if <code>sqlType</code> is
     *                                         a <code>ARRAY</code>, <code>BLOB</code>, <code>CLOB</code>,
     *                                         <code>DATALINK</code>, <code>JAVA_OBJECT</code>, <code>NCHAR</code>,
     *                                         <code>NCLOB</code>, <code>NVARCHAR</code>, <code>LONGNVARCHAR</code>,
     *                                         <code>REF</code>, <code>ROWID</code>, <code>SQLXML</code>
     *                                         or  <code>STRUCT</code> data type and the JDBC driver does not support
     *                                         this data type
     */
    public ParamBuilder setNull(int sqlType) throws SQLException {
        stmt.setNull(index(), sqlType);
        return this;
    }

    /**
     * Sets the designated parameter to the given Java <code>boolean</code> value.
     * The driver converts this
     * to an SQL <code>BIT</code> or <code>BOOLEAN</code> value when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement;
     *                      if a database access error occurs or
     *                      this method is called on a closed <code>PreparedStatement</code>
     */
    public ParamBuilder setBoolean(Boolean x) throws SQLException {
        if (x == null) return setNull(Types.BOOLEAN);
        stmt.setBoolean(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given Java <code>byte</code> value.
     * The driver converts this
     * to an SQL <code>TINYINT</code> value when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed <code>PreparedStatement</code>
     */
    public ParamBuilder setByte(Byte x) throws SQLException {
        if (x == null) return setNull(Types.BIT);
        stmt.setByte(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given Java <code>short</code> value.
     * The driver converts this
     * to an SQL <code>SMALLINT</code> value when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed <code>PreparedStatement</code>
     */
    public ParamBuilder setShort(Short x) throws SQLException {
        if (x == null) return setNull(Types.INTEGER);
        stmt.setShort(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given Java <code>int</code> value.
     * The driver converts this
     * to an SQL <code>INTEGER</code> value when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed <code>PreparedStatement</code>
     */
    public ParamBuilder setInt(Integer x) throws SQLException {
        if (x == null) return setNull(Types.INTEGER);
        stmt.setInt(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given Java <code>long</code> value.
     * The driver converts this
     * to an SQL <code>BIGINT</code> value when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed <code>PreparedStatement</code>
     */
    public ParamBuilder setLong(Long x) throws SQLException {
        if (x == null) return setNull(Types.BIGINT);
        stmt.setLong(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given Java <code>float</code> value.
     * The driver converts this
     * to an SQL <code>REAL</code> value when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed <code>PreparedStatement</code>
     */
    public ParamBuilder setFloat(Float x) throws SQLException {
        if (x == null) return setNull(Types.FLOAT);
        stmt.setFloat(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given Java <code>double</code> value.
     * The driver converts this
     * to an SQL <code>DOUBLE</code> value when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed <code>PreparedStatement</code>
     */
    public ParamBuilder setDouble(Double x) throws SQLException {
        if (x == null) return setNull(Types.DOUBLE);
        stmt.setDouble(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given <code>java.math.BigDecimal</code> value.
     * The driver converts this to an SQL <code>NUMERIC</code> value when
     * it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed <code>PreparedStatement</code>
     */
    public ParamBuilder setBigDecimal(BigDecimal x) throws SQLException {
        if (x == null) return setNull(Types.DECIMAL);
        stmt.setBigDecimal(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given Java <code>String</code> value.
     * The driver converts this
     * to an SQL <code>VARCHAR</code> or <code>LONGVARCHAR</code> value
     * (depending on the argument's
     * size relative to the driver's limits on <code>VARCHAR</code> values)
     * when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed <code>PreparedStatement</code>
     */
    public ParamBuilder setString(String x) throws SQLException {
        if (x == null) return setNull(Types.VARCHAR);
        stmt.setString(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given Java array of bytes.  The driver converts
     * this to an SQL <code>VARBINARY</code> or <code>LONGVARBINARY</code>
     * (depending on the argument's size relative to the driver's limits on
     * <code>VARBINARY</code> values) when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed <code>PreparedStatement</code>
     */
    public ParamBuilder setBytes(byte[] x) throws SQLException {
        if (x == null) return setNull(Types.BINARY);
        stmt.setBytes(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given <code>java.sql.Date</code> value
     * using the default time zone of the virtual machine that is running
     * the application.
     * The driver converts this
     * to an SQL <code>DATE</code> value when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed <code>PreparedStatement</code>
     */
    public ParamBuilder setDate(Date x) throws SQLException {
        if (x == null) return setNull(Types.DATE);
        stmt.setDate(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given <code>java.sql.Time</code> value.
     * The driver converts this
     * to an SQL <code>TIME</code> value when it sends it to the database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed <code>PreparedStatement</code>
     */
    public ParamBuilder setTime(Time x) throws SQLException {
        if (x == null) return setNull(Types.TIME);
        stmt.setTime(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given <code>java.sql.Timestamp</code> value.
     * The driver
     * converts this to an SQL <code>TIMESTAMP</code> value when it sends it to the
     * database.
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed <code>PreparedStatement</code>
     */
    public ParamBuilder setTimestamp(Timestamp x) throws SQLException {
        if (x == null) return setNull(Types.TIMESTAMP);
        stmt.setTimestamp(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given input stream, which will have
     * the specified number of bytes.
     * When a very large ASCII value is input to a <code>LONGVARCHAR</code>
     * parameter, it may be more practical to send it via a
     * <code>java.io.InputStream</code>. Data will be read from the stream
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
     *                      this method is called on a closed <code>PreparedStatement</code>
     */
    public ParamBuilder setAsciiStream(InputStream x, int length) throws SQLException {
        if (x == null) return setNull(Types.VARCHAR);
        stmt.setAsciiStream(index(), x, length);
        return this;
    }

    /**
     * Sets the designated parameter to the given input stream, which will have
     * the specified number of bytes.
     * When a very large binary value is input to a <code>LONGVARBINARY</code>
     * parameter, it may be more practical to send it via a
     * <code>java.io.InputStream</code> object. The data will be read from the
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
     *                      this method is called on a closed <code>PreparedStatement</code>
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
     * Java <code>Object</code> types to SQL types.  The given argument
     * will be converted to the corresponding SQL type before being
     * sent to the database.
     *
     * <p>Note that this method may be used to pass database-
     * specific abstract data types, by using a driver-specific Java
     * type.
     * <p>
     * If the object is of a class implementing the interface <code>SQLData</code>,
     * the JDBC driver should call the method <code>SQLData.writeSQL</code>
     * to write it to the SQL data stream.
     * If, on the other hand, the object is of a class implementing
     * <code>Ref</code>, <code>Blob</code>, <code>Clob</code>,  <code>NClob</code>,
     * <code>Struct</code>, <code>java.net.URL</code>, <code>RowId</code>, <code>SQLXML</code>
     * or <code>Array</code>, the driver should pass it to the database as a
     * value of the corresponding SQL type.
     * <p>
     * <b>Note:</b> Not all databases allow for a non-typed Null to be sent to
     * the backend. For maximum portability, the <code>setNull</code> or the
     * <code>setObject(Object x, int sqlType)</code>
     * method should be used
     * instead of <code>setObject(Object x)</code>.
     * <p>
     * <b>Note:</b> This method throws an exception if there is an ambiguity, for example, if the
     * object is of a class implementing more than one of the interfaces named above.
     *
     * @param x the object containing the input parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs;
     *                      this method is called on a closed <code>PreparedStatement</code>
     *                      or the type of the given object is ambiguous
     */
    public ParamBuilder setObject(Object x) throws SQLException {
        stmt.setObject(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given <code>Reader</code>
     * object, which is the given number of characters long.
     * When a very large UNICODE value is input to a <code>LONGVARCHAR</code>
     * parameter, it may be more practical to send it via a
     * <code>java.io.Reader</code> object. The data will be read from the stream
     * as needed until end-of-file is reached.  The JDBC driver will
     * do any necessary conversion from UNICODE to the database char format.
     *
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     *
     * @param reader the <code>java.io.Reader</code> object that contains the
     *               Unicode data
     * @param length the number of characters in the stream
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed <code>PreparedStatement</code>
     * @since 1.2
     */
    public ParamBuilder setCharacterStream(Reader reader, int length) throws SQLException {
        if (reader == null) return setNull(Types.VARCHAR);
        stmt.setCharacterStream(index(), reader, length);
        return this;
    }

    /**
     * Sets the designated parameter to the given
     * <code>REF(&lt;structured-type&gt;)</code> value.
     * The driver converts this to an SQL <code>REF</code> value when it
     * sends it to the database.
     *
     * @param x an SQL <code>REF</code> value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed <code>PreparedStatement</code>
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.2
     */
    public ParamBuilder setRef(Ref x) throws SQLException {
        if (x == null) return setNull(Types.REF);
        stmt.setRef(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given <code>java.sql.Blob</code> object.
     * The driver converts this to an SQL <code>BLOB</code> value when it
     * sends it to the database.
     *
     * @param x a <code>Blob</code> object that maps an SQL <code>BLOB</code> value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed <code>PreparedStatement</code>
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.2
     */
    public ParamBuilder setBlob(Blob x) throws SQLException {
        if (x == null) return setNull(Types.BLOB);
        stmt.setBlob(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given <code>java.sql.Clob</code> object.
     * The driver converts this to an SQL <code>CLOB</code> value when it
     * sends it to the database.
     *
     * @param x a <code>Clob</code> object that maps an SQL <code>CLOB</code> value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed <code>PreparedStatement</code>
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.2
     */
    public ParamBuilder setClob(Clob x) throws SQLException {
        if (x == null) return setNull(Types.CLOB);
        stmt.setClob(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given <code>java.sql.Array</code> object.
     * The driver converts this to an SQL <code>ARRAY</code> value when it
     * sends it to the database.
     *
     * @param x an <code>Array</code> object that maps an SQL <code>ARRAY</code> value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed <code>PreparedStatement</code>
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.2
     */
    public ParamBuilder setArray(Array x) throws SQLException {
        if (x == null) return setNull(Types.ARRAY);
        stmt.setArray(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given <code>java.net.URL</code> value.
     * The driver converts this to an SQL <code>DATALINK</code> value
     * when it sends it to the database.
     *
     * @param x the <code>java.net.URL</code> object to be set
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed <code>PreparedStatement</code>
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.4
     */
    public ParamBuilder setURL(URL x) throws SQLException {
        if (x == null) return setNull(Types.DATALINK);
        stmt.setURL(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given <code>java.sql.RowId</code> object. The
     * driver converts this to a SQL <code>ROWID</code> value when it sends it
     * to the database
     *
     * @param x the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed <code>PreparedStatement</code>
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setRowId(RowId x) throws SQLException {
        if (x == null) return setNull(Types.ROWID);
        stmt.setRowId(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given <code>String</code> object.
     * The driver converts this to a SQL <code>NCHAR</code> or
     * <code>NVARCHAR</code> or <code>LONGNVARCHAR</code> value
     * (depending on the argument's
     * size relative to the driver's limits on <code>NVARCHAR</code> values)
     * when it sends it to the database.
     *
     * @param value the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if the driver does not support national
     *                                         character sets;  if the driver can detect that a data conversion
     *                                         error could occur; if a database access error occurs; or
     *                                         this method is called on a closed <code>PreparedStatement</code>
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setNString(String value) throws SQLException {
        if (value == null) return setNull(Types.NVARCHAR);
        stmt.setNString(index(), value);
        return this;
    }

    /**
     * Sets the designated parameter to a <code>Reader</code> object. The
     * <code>Reader</code> reads the data till end-of-file is reached. The
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
     *                                         this method is called on a closed <code>PreparedStatement</code>
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setNCharacterStream(Reader value, long length) throws SQLException {
        if (value == null) return setNull(Types.VARCHAR);
        stmt.setNCharacterStream(index(), value, length);
        return this;
    }

    /**
     * Sets the designated parameter to a <code>java.sql.NClob</code> object. The driver converts this to a
     * SQL <code>NCLOB</code> value when it sends it to the database.
     *
     * @param value the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if the driver does not support national
     *                                         character sets;  if the driver can detect that a data conversion
     *                                         error could occur; if a database access error occurs; or
     *                                         this method is called on a closed <code>PreparedStatement</code>
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setNClob(NClob value) throws SQLException {
        if (value == null) return setNull(Types.NCLOB);
        stmt.setNClob(index(), value);
        return this;
    }

    /**
     * Sets the designated parameter to a <code>Reader</code> object.  The reader must contain  the number
     * of characters specified by length otherwise a <code>SQLException</code> will be
     * generated when the <code>PreparedStatement</code> is executed.
     * This method differs from the <code>setCharacterStream (int, Reader, int)</code> method
     * because it informs the driver that the parameter value should be sent to
     * the server as a <code>CLOB</code>.  When the <code>setCharacterStream</code> method is used, the
     * driver may have to do extra work to determine whether the parameter
     * data should be sent to the server as a <code>LONGVARCHAR</code> or a <code>CLOB</code>
     *
     * @param reader An object that contains the data to set the parameter value to.
     * @param length the number of characters in the parameter data.
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs; this method is called on
     *                                         a closed <code>PreparedStatement</code> or if the length specified is less than zero.
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setClob(Reader reader, long length) throws SQLException {
        if (reader == null) return setNull(Types.VARCHAR);
        stmt.setClob(index(), reader, length);
        return this;
    }

    /**
     * Sets the designated parameter to a <code>InputStream</code> object.
     * The {@code Inputstream} must contain  the number
     * of characters specified by length otherwise a <code>SQLException</code> will be
     * generated when the <code>PreparedStatement</code> is executed.
     * This method differs from the <code>setBinaryStream (int, InputStream, int)</code>
     * method because it informs the driver that the parameter value should be
     * sent to the server as a <code>BLOB</code>.  When the <code>setBinaryStream</code> method is used,
     * the driver may have to do extra work to determine whether the parameter
     * data should be sent to the server as a <code>LONGVARBINARY</code> or a <code>BLOB</code>
     *
     * @param inputStream An object that contains the data to set the parameter
     *                    value to.
     * @param length      the number of bytes in the parameter data.
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs;
     *                                         this method is called on a closed <code>PreparedStatement</code>;
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
     * Sets the designated parameter to a <code>Reader</code> object.  The reader must contain  the number
     * of characters specified by length otherwise a <code>SQLException</code> will be
     * generated when the <code>PreparedStatement</code> is executed.
     * This method differs from the <code>setCharacterStream (int, Reader, int)</code> method
     * because it informs the driver that the parameter value should be sent to
     * the server as a <code>NCLOB</code>.  When the <code>setCharacterStream</code> method is used, the
     * driver may have to do extra work to determine whether the parameter
     * data should be sent to the server as a <code>LONGNVARCHAR</code> or a <code>NCLOB</code>
     *
     * @param reader An object that contains the data to set the parameter value to.
     * @param length the number of characters in the parameter data.
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if the length specified is less than zero;
     *                                         if the driver does not support national character sets;
     *                                         if the driver can detect that a data conversion
     *                                         error could occur;  if a database access error occurs or
     *                                         this method is called on a closed <code>PreparedStatement</code>
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setNClob(Reader reader, long length) throws SQLException {
        if (reader == null) return setNull(Types.VARCHAR);
        stmt.setNClob(index(), reader, length);
        return this;
    }

    /**
     * Sets the designated parameter to the given <code>java.sql.SQLXML</code> object.
     * The driver converts this to an
     * SQL <code>XML</code> value when it sends it to the database.
     *
     * @param xmlObject a <code>SQLXML</code> object that maps an SQL <code>XML</code> value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs;
     *                                         this method is called on a closed <code>PreparedStatement</code>
     *                                         or the <code>java.xml.transform.Result</code>,
     *                                         <code>Writer</code> or <code>OutputStream</code> has not been closed for
     *                                         the <code>SQLXML</code> object
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
     * If the second argument is an <code>InputStream</code> then the stream must contain
     * the number of bytes specified by scaleOrLength.  If the second argument is a
     * <code>Reader</code> then the reader must contain the number of characters specified
     * by scaleOrLength. If these conditions are not true the driver will generate a
     * <code>SQLException</code> when the prepared statement is executed.
     *
     * <p>The given Java object will be converted to the given targetSqlType
     * before being sent to the database.
     * <p>
     * If the object has a custom mapping (is of a class implementing the
     * interface <code>SQLData</code>),
     * the JDBC driver should call the method <code>SQLData.writeSQL</code> to
     * write it to the SQL data stream.
     * If, on the other hand, the object is of a class implementing
     * <code>Ref</code>, <code>Blob</code>, <code>Clob</code>,  <code>NClob</code>,
     * <code>Struct</code>, <code>java.net.URL</code>,
     * or <code>Array</code>, the driver should pass it to the database as a
     * value of the corresponding SQL type.
     *
     * <p>Note that this method may be used to pass database-specific
     * abstract data types.
     *
     * @param x             the object containing the input parameter value
     * @param targetSqlType the SQL type (as defined in java.sql.Types) to be
     *                      sent to the database. The scale argument may further qualify this type.
     * @param scaleOrLength for <code>java.sql.Types.DECIMAL</code>
     *                      or <code>java.sql.Types.NUMERIC types</code>,
     *                      this is the number of digits after the decimal point. For
     *                      Java Object types <code>InputStream</code> and <code>Reader</code>,
     *                      this is the length
     *                      of the data in the stream or reader.  For all other types,
     *                      this value will be ignored.
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs;
     *                                         this method is called on a closed <code>PreparedStatement</code> or
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
     * When a very large ASCII value is input to a <code>LONGVARCHAR</code>
     * parameter, it may be more practical to send it via a
     * <code>java.io.InputStream</code>. Data will be read from the stream
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
     *                      this method is called on a closed <code>PreparedStatement</code>
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
     * When a very large binary value is input to a <code>LONGVARBINARY</code>
     * parameter, it may be more practical to send it via a
     * <code>java.io.InputStream</code> object. The data will be read from the
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
     *                      this method is called on a closed <code>PreparedStatement</code>
     * @since 1.6
     */
    public ParamBuilder setBinaryStream(InputStream x, long length) throws SQLException {
        if (x == null) return setNull(Types.LONGVARBINARY);
        stmt.setBinaryStream(index(), x, length);
        return this;
    }

    /**
     * Sets the designated parameter to the given <code>Reader</code>
     * object, which is the given number of characters long.
     * When a very large UNICODE value is input to a <code>LONGVARCHAR</code>
     * parameter, it may be more practical to send it via a
     * <code>java.io.Reader</code> object. The data will be read from the stream
     * as needed until end-of-file is reached.  The JDBC driver will
     * do any necessary conversion from UNICODE to the database char format.
     *
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     *
     * @param reader the <code>java.io.Reader</code> object that contains the
     *               Unicode data
     * @param length the number of characters in the stream
     * @return ParamBuilder with values set.
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *                      marker in the SQL statement; if a database access error occurs or
     *                      this method is called on a closed <code>PreparedStatement</code>
     * @since 1.6
     */
    public ParamBuilder setCharacterStream(Reader reader, long length) throws SQLException {
        if (reader == null) return setNull(Types.LONGVARCHAR);
        stmt.setCharacterStream(index(), reader, length);
        return this;
    }

    /**
     * Sets the designated parameter to the given input stream.
     * When a very large ASCII value is input to a <code>LONGVARCHAR</code>
     * parameter, it may be more practical to send it via a
     * <code>java.io.InputStream</code>. Data will be read from the stream
     * as needed until end-of-file is reached.  The JDBC driver will
     * do any necessary conversion from ASCII to the database char format.
     *
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     * <P><B>Note:</B> Consult your JDBC driver documentation to determine if
     * it might be more efficient to use a version of
     * <code>setAsciiStream</code> which takes a length parameter.
     *
     * @param x the Java input stream that contains the ASCII parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed <code>PreparedStatement</code>
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
     * When a very large binary value is input to a <code>LONGVARBINARY</code>
     * parameter, it may be more practical to send it via a
     * <code>java.io.InputStream</code> object. The data will be read from the
     * stream as needed until end-of-file is reached.
     *
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     * <P><B>Note:</B> Consult your JDBC driver documentation to determine if
     * it might be more efficient to use a version of
     * <code>setBinaryStream</code> which takes a length parameter.
     *
     * @param x the java input stream which contains the binary parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed <code>PreparedStatement</code>
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setBinaryStream(InputStream x) throws SQLException {
        if (x == null) return setNull(Types.LONGVARBINARY);
        stmt.setBinaryStream(index(), x);
        return this;
    }

    /**
     * Sets the designated parameter to the given <code>Reader</code>
     * object.
     * When a very large UNICODE value is input to a <code>LONGVARCHAR</code>
     * parameter, it may be more practical to send it via a
     * <code>java.io.Reader</code> object. The data will be read from the stream
     * as needed until end-of-file is reached.  The JDBC driver will
     * do any necessary conversion from UNICODE to the database char format.
     *
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     * <P><B>Note:</B> Consult your JDBC driver documentation to determine if
     * it might be more efficient to use a version of
     * <code>setCharacterStream</code> which takes a length parameter.
     *
     * @param reader the <code>java.io.Reader</code> object that contains the
     *               Unicode data
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs or
     *                                         this method is called on a closed <code>PreparedStatement</code>
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setCharacterStream(Reader reader) throws SQLException {
        if (reader == null) return setNull(Types.LONGVARCHAR);
        stmt.setCharacterStream(index(), reader);
        return this;
    }

    /**
     * Sets the designated parameter to a <code>Reader</code> object. The
     * <code>Reader</code> reads the data till end-of-file is reached. The
     * driver does the necessary conversion from Java character format to
     * the national character set in the database.
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     * <P><B>Note:</B> Consult your JDBC driver documentation to determine if
     * it might be more efficient to use a version of
     * <code>setNCharacterStream</code> which takes a length parameter.
     *
     * @param value the parameter value
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if the driver does not support national
     *                                         character sets;  if the driver can detect that a data conversion
     *                                         error could occur; if a database access error occurs; or
     *                                         this method is called on a closed <code>PreparedStatement</code>
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     * @since 1.6
     */
    public ParamBuilder setNCharacterStream(Reader value) throws SQLException {
        if (value == null) return setNull(Types.VARCHAR);
        stmt.setNCharacterStream(index(), value);
        return this;
    }

    /**
     * Sets the designated parameter to a <code>Reader</code> object.
     * This method differs from the <code>setCharacterStream (int, Reader)</code> method
     * because it informs the driver that the parameter value should be sent to
     * the server as a <code>CLOB</code>.  When the <code>setCharacterStream</code> method is used, the
     * driver may have to do extra work to determine whether the parameter
     * data should be sent to the server as a <code>LONGVARCHAR</code> or a <code>CLOB</code>
     *
     * <P><B>Note:</B> Consult your JDBC driver documentation to determine if
     * it might be more efficient to use a version of
     * <code>setClob</code> which takes a length parameter.
     *
     * @param reader An object that contains the data to set the parameter value to.
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs; this method is called on
     *                                         a closed <code>PreparedStatement</code>or if parameterIndex does not correspond to a parameter
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
     * Sets the designated parameter to a <code>InputStream</code> object.
     * This method differs from the <code>setBinaryStream (int, InputStream)</code>
     * method because it informs the driver that the parameter value should be
     * sent to the server as a <code>BLOB</code>.  When the <code>setBinaryStream</code> method is used,
     * the driver may have to do extra work to determine whether the parameter
     * data should be sent to the server as a <code>LONGVARBINARY</code> or a <code>BLOB</code>
     *
     * <P><B>Note:</B> Consult your JDBC driver documentation to determine if
     * it might be more efficient to use a version of
     * <code>setBlob</code> which takes a length parameter.
     *
     * @param inputStream An object that contains the data to set the parameter
     *                    value to.
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement; if a database access error occurs;
     *                                         this method is called on a closed <code>PreparedStatement</code> or
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
     * Sets the designated parameter to a <code>Reader</code> object.
     * This method differs from the <code>setCharacterStream (int, Reader)</code> method
     * because it informs the driver that the parameter value should be sent to
     * the server as a <code>NCLOB</code>.  When the <code>setCharacterStream</code> method is used, the
     * driver may have to do extra work to determine whether the parameter
     * data should be sent to the server as a <code>LONGNVARCHAR</code> or a <code>NCLOB</code>
     * <P><B>Note:</B> Consult your JDBC driver documentation to determine if
     * it might be more efficient to use a version of
     * <code>setNClob</code> which takes a length parameter.
     *
     * @param reader An object that contains the data to set the parameter value to.
     * @return ParamBuilder with values set.
     * @throws SQLException                    if parameterIndex does not correspond to a parameter
     *                                         marker in the SQL statement;
     *                                         if the driver does not support national character sets;
     *                                         if the driver can detect that a data conversion
     *                                         error could occur;  if a database access error occurs or
     *                                         this method is called on a closed <code>PreparedStatement</code>
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
