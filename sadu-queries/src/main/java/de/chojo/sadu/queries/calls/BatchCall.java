/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.calls;

import de.chojo.sadu.queries.api.call.Call;
import de.chojo.sadu.queries.api.call.calls.Calls;
import de.chojo.sadu.queries.call.CallImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The BatchCall class represents a collection of calls on the same query.
 * It allows adding multiple Call objects and combining them into a single batch.
 */
public class BatchCall implements Calls {
    private final List<Call> calls;

    /**
     * Constructs a new instance of BatchCall and adds a single Call to it.
     *
     * @param call The Call object to be added to the BatchCall.
     */
    public BatchCall(Call call) {
        this();
        calls.add(call);
    }

    /**
     * The BatchCall class represents a collection of calls on the same query.
     * It allows adding multiple Call objects and combining them into a single batch.
     */
    public BatchCall() {
        calls = new ArrayList<>();
    }

    /**
     * Represents a batch call, which is a collection of individual query calls.
     */
    public BatchCall(List<Call> calls) {
        this.calls = calls;
    }

    /**
     * Adds a {@link CallImpl} object to the list of calls.
     *
     * @param call The call to be added to the list.
     * @return The updated object.
     */
    public BatchCall add(Call call) {
        calls.add(call);
        return this;
    }

    /**
     * Combines the calls from the given BatchCall object with the calls of the current BatchCall object.
     * The calls are added to the current BatchCall object.
     *
     * @param call The BatchCall object whose calls will be combined with the current BatchCall object's calls.
     * @return The current BatchCall object after combining the calls.
     */
    public BatchCall combine(BatchCall call) {
        calls.addAll(call.calls());
        return this;
    }


    @Override
    public List<Call> calls() {
        return Collections.unmodifiableList(calls);
    }
}
