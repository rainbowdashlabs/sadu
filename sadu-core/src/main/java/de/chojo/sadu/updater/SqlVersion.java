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

    public SqlVersion ofPatch(int patch) {
        return new SqlVersion(major, patch);
    }

    public SqlVersion increasePatch(int patch) {
        return new SqlVersion(major, patch + 1);
    }

    public SqlVersion nextMajor() {
        return new SqlVersion(major + 1, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SqlVersion that = (SqlVersion) o;

        if (major != that.major) return false;
        return patch == that.patch;
    }

    @Override
    public int hashCode() {
        int result = major;
        result = 31 * result + patch;
        return result;
    }

    @Override
    public String toString() {
        return "%s.%s".formatted(major, patch);
    }
}
