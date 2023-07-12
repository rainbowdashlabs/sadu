/*
 *     SPDX-License-Identifier: LGPL-3.0-only
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.updater;

class Patch {
    private final int major;
    private final int patch;
    private final String query;

    public Patch(int major, int patch, String query) {
        this.major = major;
        this.patch = patch;
        this.query = query;
    }

    public int major() {
        return major;
    }

    public int patch() {
        return patch;
    }

    public String query() {
        return query;
    }

    public SqlVersion version(){
        return new SqlVersion(major, patch);
    }
}
