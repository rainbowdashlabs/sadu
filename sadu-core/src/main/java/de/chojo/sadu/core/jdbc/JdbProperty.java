/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.core.jdbc;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * A key value pair used to represent url parameter
 *
 * @param <T> type of property
 */
public class JdbProperty<T> {
    private final String key;
    private final T value;

    /**
     * Creates a new jdbc property
     *
     * @param key   key
     * @param value value
     */
    public JdbProperty(String key, T value) {
        this.key = key;
        this.value = value;
    }

    /**
     * The property key
     *
     * @return key
     */
    public String key() {
        return key;
    }

    /**
     * The url encoded value of the property
     *
     * @return encoded property value
     */
    public String value() {
        return URLEncoder.encode(String.valueOf(value), StandardCharsets.UTF_8);
    }

    public String valueRaw() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JdbProperty<?> prop)) return false;

        return key.equals(prop.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
