/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.calls;

import de.chojo.sadu.queries.call.Call;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BatchCall implements Calls {
    private final List<Call> calls;

    public BatchCall(Call call) {
        this.calls = new ArrayList<>();
        calls.add(call);
    }

    public BatchCall(List<Call> calls) {
        this.calls = calls;
    }

    public BatchCall add(Call call) {
        calls.add(call);
        return this;
    }


    @Override
    public List<Call> calls() {
        return Collections.unmodifiableList(calls);
    }
}
