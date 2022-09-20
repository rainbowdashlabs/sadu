/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.jdbc;

import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckReturnValue;
import java.sql.Driver;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A basic jdbc config
 *
 * @param <T> type of config
 */
public abstract class JdbcConfig<T extends JdbcConfig<?>> {
    private final Set<JdbProperty<?>> parameter = new LinkedHashSet<>();

    private String driverClass;

    /**
     * Set the driver class which should be used.
     * <p>
     * This needs to be set when relocation is used.
     * <p>
     * Usage of {@link #driverClass(Class)} is recommended.
     *
     * @param driverClass fully qualified path of the driver class
     * @return builder class
     */
    public T driverClass(@NotNull String driverClass) {
        this.driverClass = driverClass;
        return self();
    }

    /**
     * Set the driver class which should be used.
     * <p>
     * This needs to be set when relocation is used.
     *
     * @param driverClass Class reference of the driver
     * @param <V>         Class of type Driver
     * @return builder class
     */
    public <V extends Driver> T driverClass(@NotNull Class<V> driverClass) {
        return driverClass(driverClass.getName());
    }

    /**
     * Returns the full path of the driver class
     *
     * @return driver class
     */
    protected abstract String defaultDriverClass();

    /**
     * Get the fully qualified path of the driver class
     *
     * @return path of driver class
     */
    public final String driverClass() {
        return Optional.ofNullable(driverClass).orElse(defaultDriverClass());
    }

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
     *
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
