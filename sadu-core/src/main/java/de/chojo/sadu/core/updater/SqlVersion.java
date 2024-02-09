/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.core.updater;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Class representing a version maintained by the SqlUpdaterBuilder
 * <p>
 * A version id defined by a major and a patch version.
 */
public class SqlVersion implements Comparable<SqlVersion> {
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

    public boolean isNewer(SqlVersion version) {
        return compareTo(version) > 0;
    }

    public boolean isOlder(SqlVersion version) {
        return compareTo(version) < 0;
    }

    @Override
    public int compareTo(@NotNull SqlVersion o) {
        int compare = Integer.compare(major, o.major);
        if (compare != 0) {
            return compare;
        }
        return Integer.compare(patch, o.patch);
    }

    public static SqlVersion load() throws IOException {
        return load(SqlVersion.class.getClassLoader());
    }

    public static SqlVersion load(ClassLoader classLoader) throws IOException {
        var version = "";
        try (var versionFile = classLoader.getResourceAsStream("database/version")) {
            version = new String(versionFile.readAllBytes(), StandardCharsets.UTF_8).trim();
        }

        var ver = version.split("\\.");
        return new SqlVersion(Integer.parseInt(ver[0]), Integer.parseInt(ver[1]));

    }
}
