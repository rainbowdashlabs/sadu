package de.chojo.sadu.queries.stages.parsed;

import de.chojo.sadu.exceptions.ThrowingFunction;
import de.chojo.sadu.queries.TokenizedQuery;
import de.chojo.sadu.queries.call.Call;
import de.chojo.sadu.queries.calls.SingletonCall;
import de.chojo.sadu.queries.stages.ParsedQuery;
import de.chojo.sadu.queries.stages.base.ConnectionProvider;
import de.chojo.sadu.queries.stages.mapped.ManipulationQuery;
import de.chojo.sadu.queries.stages.mapped.MappedQuery;
import de.chojo.sadu.wrapper.util.Row;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class CalledSingletonQuery implements ConnectionProvider {
    private final ParsedQuery<?> query;
    private final SingletonCall call;

    public CalledSingletonQuery(ParsedQuery<?> query, SingletonCall call) {
        this.query = query;
        this.call = call;
    }

    public <V> MappedQuery<V> map(ThrowingFunction<V, Row, SQLException> mapper) {
        return new MappedQuery<>(this, mapper);
    }

    public ManipulationQuery insert() {
        var changed = 0;
        try (var stmt = query.connection().prepareStatement(query.sql().tokenizedSql())) {
            //TODO find way to return generated keys
            call.call().apply(query.sql(), stmt);
            changed = stmt.executeUpdate();
        } catch (SQLException ex) {
            // TODO: logging
        }
        return new ManipulationQuery(changed);
    }

    public ManipulationQuery update() {
        var changed = 0;
        try (var stmt = query.connection().prepareStatement(query.sql().tokenizedSql())) {
            call.call().apply(query.sql(), stmt);
            changed = stmt.executeUpdate();
        } catch (SQLException ex) {
            // TODO: logging
        }
        return new ManipulationQuery(changed);
    }

    public ManipulationQuery delete() {
        return update();
    }

    @Override
    public DataSource source() {
        return query.source();
    }

    @Override
    public Connection connection() throws SQLException {
        return query.connection();
    }

    public ParsedQuery<?> query() {
        return query;
    }

    public TokenizedQuery sql() {
        return query.sql();
    }

    public Call call() {
        return call.call();
    }
}
