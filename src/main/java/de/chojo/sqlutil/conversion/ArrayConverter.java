package de.chojo.sqlutil.conversion;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * Class to convert sql arrays to java arrays or collections and vice versa.
 */
public final class ArrayConverter {
    /**
     * Convert a SQL {@link Array} into a java array of the requested return type.
     *
     * @param array array
     * @param <T>   type of required array
     * @return array with the requested type
     * @throws SQLException                             if an error occurs while attempting to access the array
     * @throws java.sql.SQLFeatureNotSupportedException if the JDBC driver does not support this method
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Array array) throws SQLException {
        return (T[]) array.getArray();
    }

    /**
     * Convert a SQL {@link Array} into a java array of the requested return type.
     *
     * @param array array
     * @param <T>   type of required array
     * @return array with the requested type
     * @throws SQLException                             if an error occurs while attempting to access the array
     * @throws java.sql.SQLFeatureNotSupportedException if the JDBC driver does not support this method
     */
    @SuppressWarnings("unchecked")
    public static <T> Collection<T> toCollection(Array array) throws SQLException {
        return List.of((T[]) array.getArray());
    }

    /**
     * Convert a SQL {@link Array} into a java array of the requested return type.
     *
     * @param array    array
     * @param supplier supplier for the collection
     * @param <T>      type of required array
     * @return array with the requested type
     * @throws SQLException                             if an error occurs while attempting to access the array
     * @throws java.sql.SQLFeatureNotSupportedException if the JDBC driver does not support this method
     */
    @SuppressWarnings("unchecked")
    public static <T extends Collection<T>> T toCollection(Array array, Supplier<T> supplier) throws SQLException {
        var collection = supplier.get();
        collection.addAll(List.of((T[]) array.getArray()));
        return collection;
    }

    /**
     * Convert a java array to an sql array of the required type.
     *
     * @param conn  connection
     * @param type  type of array
     * @param array array
     * @return sql array
     * @throws SQLException                             if a database error occurs, the JDBC type is not appropriate for the typeName and the conversion is not supported, the typeName is null or this method is called on a closed connection.
     * @throws java.sql.SQLFeatureNotSupportedException if the JDBC driver does not support this data type
     */
    public static Array toArray(Connection conn, SQLType type, Object[] array) throws SQLException {
        return conn.createArrayOf(type.getName(), array);
    }

    /**
     * Convert a java collection to an sql array of the required type.
     *
     * @param conn       connection
     * @param type       type of array
     * @param collection collection
     * @return sql array
     * @throws SQLException                             if a database error occurs, the JDBC type is not appropriate for the typeName and the conversion is not supported, the typeName is null or this method is called on a closed connection.
     * @throws java.sql.SQLFeatureNotSupportedException if the JDBC driver does not support this data type
     */
    public static Array toArray(Connection conn, SQLType type, Collection<?> collection) throws SQLException {
        return toArray(conn, type, collection.toArray());
    }
}
