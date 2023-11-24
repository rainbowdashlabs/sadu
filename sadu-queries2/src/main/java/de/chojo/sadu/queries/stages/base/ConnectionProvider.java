package de.chojo.sadu.queries.stages.base;

import de.chojo.sadu.base.DataSourceProvider;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider extends DataSourceProvider {
    Connection connection() throws SQLException;
}
