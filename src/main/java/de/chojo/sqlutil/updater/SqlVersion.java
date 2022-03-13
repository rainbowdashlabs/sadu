/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil.updater;

public class SqlVersion {
    private final int major;
    private final int patch;

    public SqlVersion(int major, int patch) {
        this.major = major;
        this.patch = patch;
    }

    public int major() {
        return major;
    }

    public int patch() {
        return patch;
    }
}
