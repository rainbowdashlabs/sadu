/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.call.calls;

import de.chojo.sadu.queries.api.call.Call;
import de.chojo.sadu.queries.calls.BatchCall;
import de.chojo.sadu.queries.calls.SingletonCall;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * The calls class represents the calls of the same query.
 * <p>
 * A calls object can contain one or more calls on the same query.
 */
@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface Calls {

    /**
     * Returns an instance of the SingletonCall class that represents an empty call.
     *
     * @return An instance of SingletonCall representing an empty call.
     */
    @SuppressWarnings("SameReturnValue")
    static SingletonCall empty() {
        return SingletonCall.EMPTY;
    }

    /**
     * Returns a collector that accumulates the input elements into a BatchCall object.
     * The collector takes a stream of Call objects and produces a single BatchCall object.
     *
     * @return a collector for Call objects that produces a BatchCall object
     */
    static Collector<Call, BatchCall, BatchCall> collect() {
        return new Collector<>() {
            @Override
            public Supplier<BatchCall> supplier() {
                return BatchCall::new;
            }

            @SuppressWarnings("ResultOfMethodCallIgnored")
            @Override
            public BiConsumer<BatchCall, Call> accumulator() {
                return BatchCall::add;
            }

            @Override
            public BinaryOperator<BatchCall> combiner() {
                return BatchCall::combine;
            }

            @Override
            public Function<BatchCall, BatchCall> finisher() {
                return t -> t;
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Set.of(Characteristics.IDENTITY_FINISH);
            }
        };
    }

    /**
     * Returns a list of Call objects representing the query calls.
     *
     * @return A list of Call objects.
     */
    List<Call> calls();
}
