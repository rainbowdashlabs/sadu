/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.types;

/**
 * Represents a named data type in a database.
 */
@FunctionalInterface
public interface SqlType {
    /**
     * Creates a new sql type with a name
     *
     * @param name name
     * @return new type
     */
    static SqlType ofName(String name) {
        return () -> name;
    }

    /**
     * Name of type
     *
     * @return type
     */
    String name();
}
