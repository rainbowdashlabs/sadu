/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.conversion;

import de.chojo.sadu.types.SqlType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Class to convert sql arrays to java arrays or collections and vice versa.
 */
public final class ArrayConverter {
    private ArrayConverter() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    /**
     * Convert a SQL {@link Array} into a java array of the requested return type.
     *
     * @param array array
     * @param <T>   type of required array
     * @return array with the requested type
     * @throws SQLException                    if an error occurs while attempting to access the array
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Array array) throws SQLException {
        return (T[]) array.getArray();
    }

    /**
     * Converts an array of a result set column to a java array
     *
     * @param resultSet result set
     * @param column    column name
     * @param <T>       type of result
     * @return new array
     * @throws SQLException                    if the columnLabel is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    @Nullable
    public static <T> T[] toArray(ResultSet resultSet, String column) throws SQLException {
        return toArray(resultSet, column, null);
    }

    /**
     * Converts an array of a result set column to a java array
     *
     * @param resultSet result set
     * @param column    column name
     * @param <T>       type of result
     * @return new array
     * @throws SQLException                    if the columnLabel is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     *                                         this method
     */
    @Nullable
    public static <T> T[] toArray(ResultSet resultSet, int column) throws SQLException {
        return toArray(resultSet, column, null);
    }

    /**
     * Converts a column of a result set to an java array.
     *
     * @param resultSet result set
     * @param column    name of column
     * @param def       default value
     * @param <T>       type of result
     * @return array of requested type
     * @throws SQLException                    if the columnLabel is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     */
    @SuppressWarnings("unchecked")
    @Nullable
    @Contract("_,_,!null -> !null")
    public static <T> T[] toArray(@NotNull ResultSet resultSet, int column, @Nullable T[] def) throws SQLException {
        var array = fromResultSet(resultSet, column);
        return array == null ? def : (T[]) toArray(array);
    }

    /**
     * Converts a column of a result set to an java array.
     *
     * @param resultSet result set
     * @param column    name of column
     * @param def       default value
     * @param <T>       type of result
     * @return array of requested type
     * @throws SQLException                    if the columnLabel is not valid;
     *                                         if a database access error occurs
     *                                         or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     */
    @SuppressWarnings("unchecked")
    @Nullable
    @Contract("_,_,!null -> !null")
    public static <T> T[] toArray(@NotNull ResultSet resultSet, @NotNull String column, @Nullable T[] def) throws SQLException {
        var array = fromResultSet(resultSet, column);
        return array == null ? def : (T[]) toArray(array);
    }

    /**
     * Convert a SQL {@link Array} into a java array of the requested return type.
     *
     * @param array array
     * @param <T>   type of required array
     * @return array with the requested type
     * @throws SQLException                    if an error occurs while attempting to access the array
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     */
    @SuppressWarnings("unchecked")
    public static <T> Collection<T> toCollection(Array array) throws SQLException {
        return List.of((T[]) array.getArray());
    }

    /**
     * Convert a SQL {@link Array} into a java array of the requested return type.
     *
     * @param resultSet result set
     * @param column    name of column
     * @param supplier  supplier of the collection
     * @param <T>       type of result
     * @return array with the requested type
     * @throws SQLException                    if an error occurs while attempting to access the array
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
     */
    public static <T extends Collection<T>> T toCollection(ResultSet resultSet, String column, Supplier<T> supplier) throws SQLException {
        var collection = supplier.get();
        collection.addAll(List.of(toArray(resultSet, column)));
        return collection;
    }

    /**
     * Converts a column of a result set to a set.
     *
     * @param resultSet result set
     * @param column    name of column
     * @param <T>       type of result
     * @return array as set
     * @throws SQLException                    if a database error occurs, the JDBC type is not appropriate for the typeName and the conversion is not supported, the typeName is null or this method is called on a closed connection.
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this data type
     */
    public static <T> Set<T> toSet(ResultSet resultSet, String column) throws SQLException {
        T[] objects = toArray(resultSet, column);
        return objects == null ? new HashSet<>() : Set.of();
    }

    /**
     * Converts a column of a result set to a list.
     *
     * @param resultSet result set
     * @param column    column name
     * @param <T>       type of list
     * @return new list
     * @throws SQLException                    if a database error occurs, the JDBC type is not appropriate for the typeName and the conversion is not supported, the typeName is null or this method is called on a closed connection.
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this data type
     */
    public static <T> List<T> toList(ResultSet resultSet, String column) throws SQLException {
        T[] objects = toArray(resultSet, column);
        return objects == null ? new ArrayList<>() : List.of(objects);
    }

    /**
     * Converts a column of a result set to a list.
     *
     * @param resultSet result set
     * @param column    column name
     * @param <T>       type of list
     * @return new list
     * @throws SQLException                    if a database error occurs, the JDBC type is not appropriate for the typeName and the conversion is not supported, the typeName is null or this method is called on a closed connection.
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this data type
     */
    public static <T> List<T> toList(ResultSet resultSet, int column) throws SQLException {
        T[] objects = toArray(resultSet, column);
        return objects == null ? new ArrayList<>() : List.of(objects);
    }


    /**
     * Convert a java collection to a sql array of the required type.
     *
     * @param conn       connection
     * @param type       type of array
     * @param collection collection
     * @return sql array
     * @throws SQLException                    if a database error occurs, the JDBC type is not appropriate for the typeName and the conversion is not supported, the typeName is null or this method is called on a closed connection.
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this data type
     */
    public static Array toSqlArray(Connection conn, SqlType type, Collection<?> collection) throws SQLException {
        return toSqlArray(conn, type, collection.toArray());
    }

    /**
     * Convert a java array to a sql array of the required type.
     *
     * @param conn  connection
     * @param type  type of array
     * @param array array
     * @return sql array
     * @throws SQLException                    if a database error occurs, the JDBC type is not appropriate for the typeName and the conversion is not supported, the typeName is null or this method is called on a closed connection.
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this data type
     */
    public static Array toSqlArray(Connection conn, SqlType type, Object[] array) throws SQLException {
        return conn.createArrayOf(type.name(), array);
    }

    private static Array fromResultSet(ResultSet resultSet, String column) throws SQLException {
        return resultSet.getArray(column);
    }

    private static Array fromResultSet(ResultSet resultSet, int column) throws SQLException {
        return resultSet.getArray(column);
    }
}
