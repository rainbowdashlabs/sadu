/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sqlutil.jdbc;

import de.chojo.sqlutil.databases.SqlType;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A basic jdbc config
 * @param <T> type of config
 */
public abstract class JdbcConfig<T extends JdbcConfig<?>> {
    private final Set<JdbProperty<?>> parameter = new LinkedHashSet<>();

    /**
     * Returns the driver name of the jdbc url
     *
     * @return driver
     */
    protected abstract String driver();

    /**
     * Returns the instance with the correct type.
     *
     * @return instance
     */
    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

    /**
     * Add a new parameter to the url
     *
     * @param key   key
     * @param value value
     * @param <V>   value type
     * @return builder instance
     */
    public <V> T addParameter(String key, V value) {
        if (!parameter.add(new JdbProperty<>(key, value))) {
            var property = parameter.stream().filter(e -> e.key().equals(key)).findFirst().get();
            throw new IllegalArgumentException(String.format("Parameter '%s' is already set to '%s' and can not be set to '%s'.", key, property.value(), value));
        }
        return self();
    }

    /**
     * Returns the base url without parameter
     * @return base url
     */
    protected abstract String baseUrl();

    /**
     * Build the {@link #parameter}, which were added
     *
     * @return parameter chained as a parameter string with encoded values.
     */
    protected String parameter() {
        if (parameter.isEmpty()) return "";
        return "?" + parameter.stream()
                .map(prop -> String.format("%s=%s", prop.key(), prop.value()))
                .collect(Collectors.joining("&"));
    }

    /**
     * Get the jdbc url which combines the results of {@link #baseUrl()} and {@link #parameter()}
     *
     * @return jdbc string
     */
    public String jdbcUrl() {
        return baseUrl() + parameter();
    }
}
