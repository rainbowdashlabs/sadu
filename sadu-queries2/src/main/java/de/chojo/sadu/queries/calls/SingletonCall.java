/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
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

    public Call call() {
        return call;
    }
}
