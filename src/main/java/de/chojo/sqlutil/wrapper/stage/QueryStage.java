package de.chojo.sqlutil.wrapper.stage;

import de.chojo.sqlutil.wrapper.QueryBuilder;

/**
 * Query stage of a {@link QueryBuilder}
 *
 * @param <T> type
 */
public interface QueryStage<T> {
    /**
     * Set the query to execute.
     *
     * @param query query to set.
     * @return The {@link QueryBuilder} in a {@link StatementStage} with the query defined.
     */
    StatementStage<T> query(String query);

    /**
     * Set the query to execute.
     * <p>
     * This will also skip the statement stage.
     *
     * @param query query to set.
     * @return The {@link QueryBuilder} in a {@link ResultStage} with the query defined and no parameter set.
     */
    ResultStage<T> queryWithoutParams(String query);
}
