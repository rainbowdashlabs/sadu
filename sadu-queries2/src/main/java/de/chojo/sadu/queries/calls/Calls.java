/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.calls;

import de.chojo.sadu.queries.call.Call;

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

    @SuppressWarnings("SameReturnValue")
    static SingletonCall empty() {
        return SingletonCall.EMPTY;
    }

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

    List<Call> calls();
}
