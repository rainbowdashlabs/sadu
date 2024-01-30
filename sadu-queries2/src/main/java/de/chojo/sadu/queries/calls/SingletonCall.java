/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.calls;

import de.chojo.sadu.queries.api.call.Call;
import de.chojo.sadu.queries.api.call.calls.Calls;

import java.util.Collections;
import java.util.List;

/**
 * The SingletonCall class represents a SingletonCall object that contains a single query call representing a specific query.
 * It implements the Calls interface.
 */
public record SingletonCall(Call call) implements Calls {
    /**
     * Represents an empty call.
     * <p>
     * An empty call is a SingletonCall that contains a single query call, but with no arguments.
     * It is often used when executing a query that does not require any parameters.
     */
    public static final SingletonCall EMPTY = new SingletonCall(Call.of());

    @Override
    public List<Call> calls() {
        return Collections.singletonList(call);
    }
}
