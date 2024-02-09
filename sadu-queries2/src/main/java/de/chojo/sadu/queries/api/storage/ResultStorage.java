/*
 *     SPDX-License-Identifier: LGPL-3.0-or-later
 *
 *     Copyright (C) RainbowDashLabs and Contributor
 */

package de.chojo.sadu.queries.api.storage;

import java.util.List;
import java.util.Optional;

public interface ResultStorage {
    @SuppressWarnings("unchecked")
    <T> T get(String key);

    <T> Optional<T> getAs(String key, Class<T> clazz);

    <T> List<T> getList(String key, Class<T> clazz);
}
