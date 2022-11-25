/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.calls;

import de.chojo.sadu.queries.call.Call;

import java.util.Collections;
import java.util.List;

public class SingletonCall implements Calls {
    private final Call call;

    public SingletonCall(Call call) {
        this.call = call;
    }

    @Override
    public List<Call> calls() {
        return Collections.singletonList(call);
    }
}
