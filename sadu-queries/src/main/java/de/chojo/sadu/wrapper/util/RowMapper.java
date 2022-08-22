package de.chojo.sadu.wrapper.util;

import de.chojo.sadu.exceptions.ThrowingFunction;
import org.slf4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

public class RowMapper<T> {
    private static final Logger log = getLogger(RowMapper.class);

    private final Class<T> clazz;
    private final ThrowingFunction<T, Row, SQLException> mapper;
    private final Set<String> columns = new HashSet<>();

    public RowMapper(Class<T> clazz, ThrowingFunction<T, Row, SQLException> mapper) {
        this.clazz = clazz;
        this.mapper = mapper;
    }

    public Class<T> clazz() {
        return clazz;
    }

    public RowMapper<T> addColumn(String column) {
        columns.add(column);
        return this;
    }

    public T map(Row row) throws SQLException {
        return mapper.apply(row);
    }

    public boolean isWildcard() {
        return columns.isEmpty();
    }

    /**
     * Checks how many rows of the result set are applicable.
     *
     * @param resultSet result set
     * @return If the result set is not applicable 0 will be returned. Otherwise the count of applicable rows will be returned.
     */
    public int applicable(ResultSet resultSet) {
        Set<String> names = null;
        try {
            names = columnNames(resultSet);
        } catch (SQLException e) {
            log.error("Could not read columns", e);
            return 0;
        }
        int size = names.size();
        if (columns.size() < size) {
            // The result set has less rows than we need
            return 0;
        }
        names.retainAll(columns);

        if (names.size() != size) {
            // The result set has not all rows we need.
            return 0;
        }
        return size;
    }

    private Set<String> columnNames(ResultSet set) throws SQLException {
        Set<String> columns = new HashSet<>();
        for (int i = 1; i <= set.getMetaData().getColumnCount(); i++) {
            columns.add(set.getMetaData().getColumnLabel(i));
        }
        return columns;
    }
}
