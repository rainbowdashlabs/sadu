/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.calls;

import de.chojo.sadu.queries.calls.Calls;
import de.chojo.sadu.queries.storage.ResultStorage;

@FunctionalInterface
public interface CallSupplier<T extends Calls> {
    T supply(ResultStorage storage);
}
