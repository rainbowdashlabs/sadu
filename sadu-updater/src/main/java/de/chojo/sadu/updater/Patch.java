/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.updater;

import de.chojo.sadu.core.updater.SqlVersion;

record Patch(int major, int patch, String query) {

    public SqlVersion version() {
        return new SqlVersion(major, patch);
    }
}
