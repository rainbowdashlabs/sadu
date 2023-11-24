package de.chojo.sadu.queries.stages;

import de.chojo.sadu.base.DataSourceProvider;
import de.chojo.sadu.queries.TokenizedQuery;
import de.chojo.sadu.queries.calls.BatchCall;
import de.chojo.sadu.queries.calls.SingletonCall;
import de.chojo.sadu.queries.stages.base.ConnectionProvider;
import de.chojo.sadu.queries.stages.parsed.CalledBatchQuery;
import de.chojo.sadu.queries.stages.parsed.CalledSingletonQuery;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

public class ParsedQuery<T> implements DataSourceProvider, ConnectionProvider {
    private final Query<T> parent;
    private final TokenizedQuery sql;

    protected ParsedQuery(Query<T> parent, TokenizedQuery sql) {
        this.parent = parent;
        this.sql = sql;
    }

    public CalledBatchQuery parameter(BatchCall param) {
        return new CalledBatchQuery(this, param);
    }

    public CalledSingletonQuery parameter(SingletonCall param) {
        return new CalledSingletonQuery(this, param);
    }

    // TODO: Solve signature clash
    public CalledBatchQuery parameterBatch(Function<T, BatchCall> param) {
        // TODO: Retrieve earlier result
        return new CalledBatchQuery(this, param.apply(null));
    }

    public CalledSingletonQuery parameter(Function<T, SingletonCall> param) {
        // TODO: Retrieve earlier result
        return new CalledSingletonQuery(this, param.apply(null));
    }

    public TokenizedQuery sql() {
        return sql;
    }

    @Override
    public DataSource source() {
        return parent.source();
    }

    @Override
    public Connection connection() throws SQLException {
        return parent.connection();
    }
}
