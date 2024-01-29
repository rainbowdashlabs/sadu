/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api;

import de.chojo.sadu.queries.api.execution.writing.CalledBatchQuery;
import de.chojo.sadu.queries.api.execution.writing.CalledSingletonQuery;
import de.chojo.sadu.queries.call.Call;
import de.chojo.sadu.queries.calls.BatchCall;
import de.chojo.sadu.queries.calls.Calls;
import de.chojo.sadu.queries.calls.SingletonCall;
import de.chojo.sadu.queries.stages.calls.CallSupplier;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * A Parsed query that is ready for execution
 */
public interface ParsedQuery {
    /**
     * Define a batch call for your query.
     * This will execute the query with every argument combination that are passed here.
     *
     * @param calls the batch call to be executed
     * @return A called batch query
     */
    CalledBatchQuery batch(BatchCall calls);

    /**
     * Define a batch call for your query.
     * This will execute the query with every argument combination that are passed here.
     *
     * @param calls a call supplier that allows access to the query storage
     * @return A called batch query
     */
    CalledBatchQuery batch(CallSupplier<BatchCall> calls);

    /**
     * Define a batch call for your query.
     * This will execute the query with every argument combination that are passed here.
     *
     * @param calls a list of calls
     * @return A called batch query
     */
    default CalledBatchQuery batch(List<Call> calls) {
        return batch(new BatchCall(calls));
    }

    /**
     * Define a batch call for your query.
     * This will execute the query with every argument combination that are passed here.
     *
     * @param calls a stream of calls
     * @return A called batch query
     */
    default CalledBatchQuery batch(Stream<Call> calls) {
        return batch(calls.collect(Calls.collect()));
    }

    /**
     * Define a batch call for your query.
     * This will execute the query with every argument combination that are passed here.
     *
     * @param calls any number of calls
     * @return A called batch query
     */
    default CalledBatchQuery batch(Call... calls) {
        return batch(Arrays.stream(calls).toList());
    }

    /**
     * Define a call for your query.
     * This will execute the query with your arguments.
     *
     * @param call a singleton call
     * @return A called batch query
     */
    CalledSingletonQuery single(SingletonCall call);

    /**
     * Define a call without any parameter set.
     *
     * @return A called batch query
     */
    default CalledSingletonQuery single() {
        return single(Calls.empty());
    }

    /**
     * Define a call for your query.
     * This will execute the query with your arguments.
     *
     * @param call a call
     * @return A called singleton query
     */
    default CalledSingletonQuery single(Call call) {
        return single(Calls.single(call));
    }

    /**
     * Define a call for your query.
     * This will execute the query with your arguments.
     *
     * @param call a call supplier that allows access to the query storage
     * @return A called singleton query
     */
    CalledSingletonQuery single(CallSupplier<SingletonCall> call);
}
