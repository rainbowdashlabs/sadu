/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.calls;

import de.chojo.sadu.queries.call.Call;

import java.util.List;

/**
 * The calls class represents the calls of the same query.
 * <p>
 * A calls object can contain one or more calls on the same query.
 */
public interface Calls {

    static BatchCall batch(Call call) {
        return new BatchCall(call);
    }

    static BatchCall batch(Call... call) {
        return batch(List.of(call));
    }
    static BatchCall batch(List<Call> call) {
        return new BatchCall(call);
    }

    static SingletonCall call(Call call) {
        return new SingletonCall(call);
    }

    List<Call> calls();
}
