package de.chojo.sqlutil.wrapper.stage;

import de.chojo.sqlutil.exceptions.ThrowingFunction;
import de.chojo.sqlutil.wrapper.QueryBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a ResultStage of a {@link QueryBuilder}.
 * <p>
 * A ResultStage is used to read the result set, perform updates or append another query.
 *
 * @param <T> type of ResultStage
 */
public interface ResultStage<T> {

    /**
     * Extract results from a results set.
     * <p>
     * This function should not loop through the results set.
     * <p>
     * It should only transform the current row to the requested object.
     *
     * @param mapper mapper to map the current row.
     * @return The {@link QueryBuilder} in a {@link RetrievalStage} to retrieve the row/s.
     */
    RetrievalStage<T> readRow(ThrowingFunction<T, ResultSet, SQLException> mapper);

    /**
     * Mark this query as update query.
     *
     * @return The {@link QueryBuilder} in a {@link UpdateStage} to update the data.
     */
    UpdateStage update();

    /**
     * Mark this query as deletion query. Alias for {@link #update()}
     *
     * @return The {@link QueryBuilder} in a {@link UpdateStage} to update the data.
     */
    default UpdateStage delete() {
        return update();
    }

    /**
     * Insert data into a table. Alias for {@link #update()}
     *
     * @return The {@link QueryBuilder} in a {@link UpdateStage} to update the data.
     */
    default UpdateStage insert() {
        return update();
    }

    /**
     * Append another query to the query builder.
     *
     * @return The {@link QueryBuilder} in a {@link QueryStage}
     */
    QueryStage<T> append();
}
