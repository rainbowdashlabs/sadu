/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.core.jdbc;

import org.jetbrains.annotations.NotNull;

import java.sql.Driver;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A base builder to create a jdbc url.
 *
 * @param <T> type of config
 */
public abstract class JdbcConfig<T extends JdbcConfig<?>> {
    private final Set<JdbProperty<?>> parameter = new LinkedHashSet<>();

    private String driverClass;

    public JdbcConfig() {}

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
            var property = parameter.stream()
                    .filter(e -> e.key().equals(key))
                    .findFirst()
                    .orElseThrow();
            throw new IllegalArgumentException(String.format(
                    "Parameter '%s' is already set to '%s' and can not be set to '%s'.", key, property.value(), value));
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
        return "?"
                + parameter.stream()
                        .map(prop -> String.format("%s=%s", prop.key(), prop.value()))
                        .collect(Collectors.joining("&"));
    }

    /**
     * Removes the parameter from the parameter list
     *
     * @param key parameter key that should be removed
     * @return the value associated with the key or null
     */
    protected JdbProperty<?> removeParameter(String key) {
        Optional<JdbProperty<?>> match =
                parameter.stream().filter(e -> e.key().equals(key)).findFirst();
        match.ifPresent(v -> parameter.removeIf(e -> e.key().equals(key)));
        return match.orElse(null);
    }

    protected Map<String, String> parameterRaw() {
        return parameter.stream().collect(Collectors.toMap(JdbProperty::key, JdbProperty::value));
    }

    /**
     * Whether the parameters are part of the {@link #jdbcUrl()}.
     * <p>
     * Drivers that decode parameter values from the url get them url encoded in the url. Drivers that take values from
     * the url literally override this with {@code false} and receive the parameters as {@link #driverProperties()}.
     *
     * @return true if the parameters are written into the url
     */
    protected boolean parametersInUrl() {
        return true;
    }

    /**
     * The parameters which are passed to the driver as connection properties instead of the url.
     * <p>
     * The values are not encoded. The map is empty when the parameters are part of the {@link #jdbcUrl()}.
     *
     * @return parameter keys mapped to their values, in the order they were added
     */
    public Map<String, String> driverProperties() {
        if (parametersInUrl()) return Map.of();
        Map<String, String> properties = new LinkedHashMap<>();
        parameter.forEach(property -> properties.put(property.key(), property.valueRaw()));
        return properties;
    }

    /**
     * Get the jdbc url which combines the results of {@link #baseUrl()} and {@link #parameter()}.
     * <p>
     * Contains no parameters for drivers which receive them as {@link #driverProperties()}.
     *
     * @return jdbc string
     */
    public String jdbcUrl() {
        return parametersInUrl() ? baseUrl() + parameter() : baseUrl();
    }
}
