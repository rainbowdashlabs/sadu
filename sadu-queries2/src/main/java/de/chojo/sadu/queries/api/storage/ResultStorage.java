package de.chojo.sadu.queries.api.storage;

import java.util.List;
import java.util.Optional;

public interface ResultStorage {
    @SuppressWarnings("unchecked")
    <T> T get(String key);

    <T> Optional<T> getAs(String key, Class<T> clazz);

    <T> List<T> getList(String key, Class<T> clazz);
}
