/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.mapper;

import de.chojo.sadu.mapper.rowmapper.RowMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * The config of a {@link RowMapper}.
 */
public class MapperConfig implements Cloneable {
    public static final MapperConfig DEFAULT = new MapperConfig();
    private Map<String, String> aliases = new HashMap<>();
    private boolean strict = false;

    public Map<String, String> aliases() {
        return aliases;
    }

    /**
     * Adds an alias for a column.
     * <p>
     * This is helpful to resolve object with the same structure but different column names.
     * <p>
     * {@code record DateCount(LocalDateTime time, int count ){}}
     *
     * <pre>
     * {@code
     * SELECT month, count FROM month_stats;
     * SELECT day, count FROM day_stats;
     * }</pre>
     * <p>
     * Simply add an alias for time and set it to month or day. When your mapper now requests the time column it will get the value of month or day.
     * <p>
     * Not that this counts for every call on a row.
     *
     * @param original original column name
     * @param alias    alias of column
     */
    public MapperConfig addAlias(String original, String alias) {
        aliases.put(original, alias);
        return this;
    }

    /**
     * When true only mappers will be used, which have a mapping value for all columns or the wild card mapper if present and no matching mapper was found.
     * @return true when strict
     */
    public boolean isStrict() {
        return strict;
    }

    /**
     * This will only use mappers which have a mapping value for all columns or the wild card mapper if present and no matching mapper was found.
     */
    public MapperConfig strict() {
        strict = true;
        return this;
    }

    @Override
    @Deprecated(forRemoval = true)
    public MapperConfig clone() {
        return copy();
    }

    public MapperConfig copy() {
        var mapperConfig = new MapperConfig();
        mapperConfig.aliases = new HashMap<>(aliases);
        mapperConfig.strict = strict;
        return mapperConfig;
    }
}
