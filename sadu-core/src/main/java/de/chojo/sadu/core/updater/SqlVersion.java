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
public record SqlVersion(int major, int patch) implements Comparable<SqlVersion> {
    /**
     * A new SqlVersion with a major and patch version
     *
     * @param major major
     * @param patch patch
     */
    public SqlVersion {
    }

    /**
     * Parses the sql version stored in database/version inside the current classloader
     * @return sql version
     * @throws IOException when the resource could not get read
     */
    public static SqlVersion load() throws IOException {
        return load(SqlVersion.class.getClassLoader());
    }

    /**
     * Parses the sql version stored in database/version inside the resources of the provided classloader
     * @param classLoader classloader to load the resource
     * @return sql version
     * @throws IOException when the resource could not get read
     */
    public static SqlVersion load(ClassLoader classLoader) throws IOException {
        var version = "";
        try (var versionFile = classLoader.getResourceAsStream("database/version")) {
            version = new String(versionFile.readAllBytes(), StandardCharsets.UTF_8);
        }

        return parse(version);
    }

    /**
     * Parses the sql version in the provided string
     * @param version version string
     * @return sql version
     */
    public static SqlVersion parse(String version) {
        var ver = version.trim().split("\\.");
        return new SqlVersion(Integer.parseInt(ver[0]), Integer.parseInt(ver[1]));
    }

    /**
     * Major version
     *
     * @return major
     */
    @Override
    public int major() {
        return major;
    }

    /**
     * Patch version
     *
     * @return patch
     */
    @Override
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
}
