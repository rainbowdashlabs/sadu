/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.wrapper.mapper;

import java.util.HashMap;
import java.util.Map;

public class MapperConfig implements Cloneable {
    public static final MapperConfig DEFAULT = new MapperConfig();
    private Map<String, String> aliases = new HashMap<>();
    private boolean strict = false;

    public Map<String, String> aliases() {
        return aliases;
    }

    /**
     * Adds a alias for a column.
     * <p>
     * This is helpful to resolve column name collisions.
     *
     * <pre>
     * {@code
     * SELECT a.title as a_title, p.title as p_title FROM applications a
     * LEFT JOIN paragraph p WHERE p.application_id = a.id
     * WHERE a.id = ?
     * }</pre>
     * <p>
     * Simply add a alias for title and set it to p_title. When your mapper now requests the title column it will get the value of p_title.
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

    public boolean isStrict() {
        return strict;
    }

    /**
     * This will only use mappers which have a mapping value for all columns or the wild card mapper if present and no matching mapper was found.
     */
    public void strict() {
        strict = true;
    }

    @Override
    public MapperConfig clone() {
        var mapperConfig = new MapperConfig();
        mapperConfig.aliases = new HashMap<>(aliases);
        mapperConfig.strict = strict;
        return mapperConfig;
    }
}
