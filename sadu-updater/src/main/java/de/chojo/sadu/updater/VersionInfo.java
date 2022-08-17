/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.updater;

class VersionInfo {
    private final int version;
    private final int patch;

    public VersionInfo(int version, int patch) {
        this.version = version;
        this.patch = patch;
    }

    public int version() {
        return version;
    }

    public int patch() {
        return patch;
    }
}
