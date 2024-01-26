/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.calls;

import de.chojo.sadu.queries.call.Call;

import java.util.Collections;
import java.util.List;

public record SingletonCall(Call call) implements Calls {
    public static final SingletonCall EMPTY = new SingletonCall(Call.of());

    @Override
    public List<Call> calls() {
        return Collections.singletonList(call);
    }
}
