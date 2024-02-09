/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.calls;

import de.chojo.sadu.queries.api.call.calls.Calls;
import de.chojo.sadu.queries.api.storage.ResultStorage;

@FunctionalInterface
public interface CallSupplier<T extends Calls> {
    T supply(ResultStorage storage);
}
