/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil.jdbc;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * A key value pair used to represent url parameter
 * @param <T>
 */
public class JdbProperty<T> {
    private final String key;
    private final T value;

    public JdbProperty(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public String key() {
        return key;
    }

    public String value() {
        return URLEncoder.encode(String.valueOf(value), StandardCharsets.UTF_8);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JdbProperty)) return false;

        var that = (JdbProperty<?>) o;

        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
