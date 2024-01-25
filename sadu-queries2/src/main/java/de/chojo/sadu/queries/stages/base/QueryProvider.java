package de.chojo.sadu.queries.stages.base;

import de.chojo.sadu.base.DataSourceProvider;
import de.chojo.sadu.queries.stages.Query;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public interface QueryProvider extends ConnectionProvider, DataSourceProvider {
    Query query();

    @Override
    default DataSource source(){
        return query().source();
    }

    @Override
    default Connection connection() throws SQLException{
        return query().connection();
    }
}
