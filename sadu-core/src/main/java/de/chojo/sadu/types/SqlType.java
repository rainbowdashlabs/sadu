/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.types;

/**
 * Represents a named data type in a database.
 */
public interface SqlType {
    /**
     * Creates a new sql type with a name
     *
     * @param name name
     * @return new type
     */
    static SqlType ofName(String name, String... alias) {
        return new SqlType() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public String[] alias() {
                return alias;
            }
        };
    }

    /**
     * Name of type
     *
     * @return type
     */
    String name();

    /**
     * Alias for a type.
     * <p>
     * E.g. INTEGER for INT
     *
     * @return array of aliases for this type
     */
    String[] alias();

    default String descr() {
        if (alias().length == 0) {
            return name();
        }
        return "%s (%s)".formatted(name(), String.join(", ", alias()));
    }

}
