/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.calls;

import de.chojo.sadu.queries.call.Call;

import java.util.List;
import java.util.function.Consumer;

/**
 * The calls class represents the calls of the same query.
 * <p>
 * A calls object can contain one or more calls on the same query.
 */
public interface Calls {

    /**
     * Execute multiple calls
     *
     * @param calls calls to execute
     * @return batch call
     */
    static BatchCall batch(Call... calls) {
        return batch(List.of(calls));
    }


    /**
     * Execute multiple calls
     *
     * @param calls calls to execute
     * @return batch call
     */
    static BatchCall batch(List<Call> calls) {
        return new BatchCall(calls);
    }

    /**
     * Execute a single call
     *
     * @param call call to execute
     * @return singleton call
     */
    static SingletonCall single(Call call) {
        return new SingletonCall(call);
    }

    /**
     * Execute a single call
     *
     * @param call call to execute
     * @return singleton call
     */
    static SingletonCall single(Consumer<Call> call) {
        Call nc = new Call();
        call.accept(nc);
        return new SingletonCall(nc);
    }

    List<Call> calls();
}
