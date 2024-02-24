/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.execution.writing;

import de.chojo.sadu.mapper.MapperConfig;
import de.chojo.sadu.mapper.rowmapper.RowMapping;
import de.chojo.sadu.queries.api.execution.reading.Reader;
import de.chojo.sadu.queries.api.results.writing.insertion.InsertionResult;
import de.chojo.sadu.queries.api.results.writing.manipulation.ManipulationResult;

/**
 * Represents a query that can be executed as a singleton call.
 */
public interface CalledSingletonQuery {

    /**
     * Maps the result of the query to a Reader of type V by applying the provided ThrowingFunction.
     *
     * @param <V> the type of objects in the Reader
     * @param row the function to apply on the result row
     * @return a Reader of type V
     */
    <V> Reader<V> map(RowMapping<V> row);

    /**
     * Maps the current result set to the specified class type and returns a Reader object.
     *
     * @param clazz the class type to map the result set to
     * @param <V>   the type of the class to map the result set to
     * @return a Reader object with the result set mapped to the specified class type
     */
    <V> Reader<V> mapAs(Class<V> clazz);

    /**
     * Maps the result of a query to the specified class using the provided MapperConfig.
     *
     * @param <V>    the type to map the query result to
     * @param clazz  the class to map the query result to
     * @param config the MapperConfig to use for mapping
     * @return a Reader object that can be used to retrieve the mapped result
     */
    <V> Reader<V> mapAs(Class<V> clazz, MapperConfig config);

    /**
     * Inserts a row into the database table represented by the initial symbol of the containing class,
     * and returns the result of the manipulation operation.
     *
     * @return The result of the manipulation operation as a {@link InsertionResult} object.
     */
    InsertionResult insertAndGetKeys();

    /**
     * Inserts a row into the database table represented by the initial symbol of the containing class,
     * and returns the result of the manipulation operation.
     *
     * @return The result of the manipulation operation as a {@link InsertionResult} object.
     */
    InsertionResult insert();

    /**
     * Updates the data in the database based on the provided query.
     *
     * @return The result of the manipulation operation as a {@link ManipulationResult} object.
     */
    ManipulationResult update();

    /**
     * Deletes rows from the table based on the specified query.
     *
     * @return The result of the manipulation operation as a {@link ManipulationResult} object.
     */
    ManipulationResult delete();
}
