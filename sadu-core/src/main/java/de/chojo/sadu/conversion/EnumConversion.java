/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2022 RainbowDashLabs and Contributor
 */

package de.chojo.sadu.conversion;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class EnumConversion {
    private EnumConversion() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    /**
     * Maps a string to the value of an enum class
     *
     * @param value value to map.
     * @param clazz enum class
     * @param <T>   enum type
     * @return the enum value of the string or null when the string is null
     */
    @Nullable
    @Contract("null,_ -> null; _,null -> fail")
    public static <T extends Enum<?>> T fromString(@Nullable String value, Class<T> clazz) {
        return fromString(value, clazz, false);
    }

    /**
     * Maps a string to the value of an enum class. Ignores casing.
     *
     * @param value value to map.
     * @param clazz enum class
     * @param <T>   enum type
     * @return the enum value of the string or null when the string is null
     */
    @Nullable
    @Contract("null,_ -> null; _,null -> fail")
    public static <T extends Enum<?>> T fromStringIgnoreCase(@Nullable String value, Class<T> clazz) {
        return fromString(value, clazz, true);
    }

    /**
     * Maps a string to the value of an enum class. Ignores casing.
     *
     * @param value value to map.
     * @param clazz enum class
     * @param <T>   enum type
     * @return the enum value of the string or null when the string is null
     * @throws IllegalArgumentException when the value could not be mapped
     * @throws NullPointerException     when the class is null
     */
    @Nullable
    @Contract("null,_,_ -> null; _,null,_ -> fail")
    public static <T extends Enum<?>> T fromString(@Nullable String value, Class<T> clazz, boolean ignoreCase) {
        Objects.requireNonNull(clazz, "Class is null");
        if (value == null) return null;
        for (T constant : clazz.getEnumConstants()) {
            if (ignoreCase ? constant.name().equalsIgnoreCase(value) : constant.name().equals(value)) {
                return constant;
            }
        }
        throw new IllegalArgumentException("Could not map " + value + " to value of " + clazz.getName());
    }
}
