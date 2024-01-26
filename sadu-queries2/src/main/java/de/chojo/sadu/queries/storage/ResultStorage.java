/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.storage;

import de.chojo.sadu.queries.api.results.reading.Result;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ResultStorage {
    private final Map<String, Result<?>> storage = new HashMap<>();

    public void store(String key, Result<?> value) {
        storage.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) retrieve(key).result();
    }

    public <T> Optional<T> getAs(String key, Class<T> clazz) {
        return Optional.ofNullable((T) retrieve(key).result());
    }

    public <T> List<T> getList(String key, Class<T> clazz) {
        return Objects.requireNonNullElse((List<T>) retrieve(key).result(), Collections.emptyList());
    }

    private Result<?> retrieve(String key) {
        Result<?> result = storage.get(key);
        if (result == null) throw new IllegalArgumentException("Key %s is not set.".formatted(key));
        return result;
    }
}
