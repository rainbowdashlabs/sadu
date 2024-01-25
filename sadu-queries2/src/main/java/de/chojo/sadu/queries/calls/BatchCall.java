/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.calls;

import de.chojo.sadu.queries.call.Call;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BatchCall implements Calls {
    private final List<Call> calls;

    public BatchCall(Call call) {
        this();
        calls.add(call);
    }

    public BatchCall() {
        calls = new ArrayList<>();
    }

    public BatchCall(List<Call> calls) {
        this.calls = calls;
    }

    public BatchCall add(Call call) {
        calls.add(call);
        return this;
    }

    public BatchCall combine(BatchCall call) {
        calls.addAll(call.calls());
        return this;
    }


    @Override
    public List<Call> calls() {
        return Collections.unmodifiableList(calls);
    }
}
