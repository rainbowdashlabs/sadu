/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.stage;

import de.chojo.sadu.exceptions.ThrowingFunction;
import de.chojo.sadu.mapper.MapperConfig;
import de.chojo.sadu.mapper.rowmapper.RowMapper;
import de.chojo.sadu.wrapper.QueryBuilder;
import de.chojo.sadu.wrapper.util.Row;

import javax.annotation.CheckReturnValue;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * Represents a ResultStage of a {@link QueryBuilder}.
 * <p>
 * A ResultStage is used to read the result set, perform updates or append another query.
 *
 * @param <T> type of ResultStage
 */
public interface ResultStage<T> {

    /**
     * Extract results from the current row and only the current row. It doesn't matter if you will get 0,1 or more results.
     * <p>
     * <b>This is not the place to define how many results you want.</b>
     * <p>
     * <b>Do not try to read multiple rows here.</b>
     * <p>
     * <b>Do not modify the underlying result set.</b>
     *
     * @param mapper mapper to map the current row.
     * @return The {@link QueryBuilder} in a {@link RetrievalStage} to retrieve the row/s.
     */
    @CheckReturnValue
    RetrievalStage<T> readRow(ThrowingFunction<T, Row, SQLException> mapper);

    /**
     * Maps the rows of the result with a previously registered {@link RowMapper}.
     * <p>
     * The best matching mapper will be used for the result set.
     *
     * @return The {@link QueryBuilder} in a {@link RetrievalStage} to retrieve the row/s.
     */
    default RetrievalStage<T> map() {
        return map(MapperConfig.DEFAULT);
    }

    /**
     * Maps the rows of the result with a previously registered {@link RowMapper}.
     * <p>
     * Maps the row and applies a mapper config to the query builder.
     *
     * @param mapperConfig mapper config
     * @return The {@link QueryBuilder} in a {@link RetrievalStage} to retrieve the row/s.
     */
    RetrievalStage<T> map(MapperConfig mapperConfig);

    /**
     * Maps the rows of the result with a previously registered {@link RowMapper}.
     * <p>
     * Maps the row and applies a mapper config to the query builder.
     *
     * @param mapperConfig modify the mapper config
     * @return The {@link QueryBuilder} in a {@link RetrievalStage} to retrieve the row/s.
     */
    default RetrievalStage<T> map(Consumer<MapperConfig> mapperConfig) {
        var config = new MapperConfig();
        mapperConfig.accept(config);
        return map(config);
    }

    /**
     * Mark this query as update query.
     *
     * @return The {@link QueryBuilder} in a {@link UpdateStage} to update the data.
     */
    @CheckReturnValue
    UpdateStage update();

    /**
     * Mark this query as deletion query. Alias for {@link #update()}
     *
     * @return The {@link QueryBuilder} in a {@link UpdateStage} to update the data.
     */
    @CheckReturnValue
    default UpdateStage delete() {
        return update();
    }

    /**
     * Insert data into a table. Alias for {@link #update()}
     *
     * @return The {@link QueryBuilder} in a {@link InsertStage} to update the data.
     */
    @CheckReturnValue
    InsertStage insert();


    /**
     * Append another query to the query builder.
     *
     * @return The {@link QueryBuilder} in a {@link QueryStage}
     */
    @CheckReturnValue
    QueryStage<T> append();
}
