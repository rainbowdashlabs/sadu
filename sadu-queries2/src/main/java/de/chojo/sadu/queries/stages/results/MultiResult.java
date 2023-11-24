/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.stages.results;

import java.util.List;

public class MultiResult<T> {
    private final T results;

    public MultiResult(T results) {
        this.results = results;
    }
}
