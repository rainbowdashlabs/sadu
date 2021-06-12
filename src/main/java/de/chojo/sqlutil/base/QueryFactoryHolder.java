package de.chojo.sqlutil.base;

import de.chojo.sqlutil.wrapper.QueryBuilderConfig;
import de.chojo.sqlutil.wrapper.QueryBuilderFactory;
import de.chojo.sqlutil.wrapper.stage.QueryStage;

import javax.sql.DataSource;

/**
 * Base class which provides a factory for easy usage.
 * <p>
 * Can be used instead of a {@link DataHolder}
 */
public class QueryFactoryHolder extends DataHolder {
    QueryBuilderFactory factory;

    /**
     * Create a new QueryFactoryholder
     *
     * @param dataSource datasource
     * @param config     factory config
     */
    public QueryFactoryHolder(DataSource dataSource, QueryBuilderConfig config) {
        super(dataSource);
        factory = new QueryBuilderFactory(config, dataSource);
    }

    /**
     * Create a new query builder with a defined return type. Use it for selects.
     *
     * @param clazz class of required return type. Doesnt matter if you want a list or single result.
     * @param <T>   type if result as class
     * @return a new query builder in a {@link QueryStage}
     */
    public <T> QueryStage<T> builder(Class<T> clazz) {
        return factory.builder(clazz);
    }

    /**
     * Create a new Query builder without a defined return type. Use it for updates.
     *
     * @return a new query builder in a {@link QueryStage}
     */
    public QueryStage<Void> builder() {
        return factory.builder();
    }
}
