/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.updater;

/**
 * Class representing a version maintained by the SqlUpdaterBuilder
 * <p>
 * A version id defined by a major and a patch version.
 */
public class SqlVersion {
    private final int major;
    private final int patch;

    /**
     * A new SqlVersion with a major and patch version
     *
     * @param major major
     * @param patch patch
     */
    public SqlVersion(int major, int patch) {
        this.major = major;
        this.patch = patch;
    }

    /**
     * Major version
     *
     * @return major
     */
    public int major() {
        return major;
    }

    /**
     * Patch version
     *
     * @return patch
     */
    public int patch() {
        return patch;
    }
}
