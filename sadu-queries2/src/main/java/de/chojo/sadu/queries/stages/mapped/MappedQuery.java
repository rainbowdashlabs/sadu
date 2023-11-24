package de.chojo.sadu.queries.stages.mapped;

import de.chojo.sadu.exceptions.ThrowingFunction;
import de.chojo.sadu.mapper.MapperConfig;
import de.chojo.sadu.queries.TokenizedQuery;
import de.chojo.sadu.queries.call.Call;
import de.chojo.sadu.queries.stages.parsed.CalledSingletonQuery;
import de.chojo.sadu.queries.stages.results.MultiResult;
import de.chojo.sadu.queries.stages.results.SingleResult;
import de.chojo.sadu.wrapper.util.Row;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MappedQuery<V> {
    private final CalledSingletonQuery query;
    private final ThrowingFunction<V, Row, SQLException> mapper;

    public MappedQuery(CalledSingletonQuery query, ThrowingFunction<V, Row, SQLException> mapper) {
        this.query = query;
        this.mapper = mapper;
    }

    // TODO: Mapping
    public SingleResult<V> one() {
        try (var stmt = connection().prepareStatement(sql().tokenizedSql())) {
            call().apply(sql(), stmt);
            ResultSet resultSet = stmt.executeQuery();
            // TODO: Get mapper config in here.
            var row = new Row(resultSet, MapperConfig.DEFAULT);
            if (resultSet.next()) return new SingleResult<>(mapper.apply(row));
        } catch (SQLException e) {
            // TODO: logging
        }
        return new SingleResult<>(null);
    }

    public MultiResult<List<V>> all() {
        var result = new ArrayList<V>();
        try (var stmt = connection().prepareStatement(sql().tokenizedSql())) {
            call().apply(sql(), stmt);
            ResultSet resultSet = stmt.executeQuery();
            // TODO: Get mapper config in here.
            var row = new Row(resultSet, MapperConfig.DEFAULT);
            while (resultSet.next()) result.add(mapper.apply(row));
        } catch (SQLException e) {
            // TODO: logging
        }
        return new MultiResult<>(result);
    }

    public Optional<V> oneAndGet() {
        return Optional.empty();
    }

    public List<V> allAndGet() {
        return Collections.emptyList();
    }

    public Connection connection() throws SQLException {
        return query.connection();
    }

    public TokenizedQuery sql() {
        return query.sql();
    }

    public Call call() {
        return query.call();
    }
}
