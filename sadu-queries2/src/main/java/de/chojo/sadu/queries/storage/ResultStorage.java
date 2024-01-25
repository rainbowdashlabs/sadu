package de.chojo.sadu.queries.storage;

import de.chojo.sadu.queries.stages.results.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultStorage {
    private final Map<String, Result<?>> storage = new HashMap<>();

    public void store(String key, Result<?> value) {
        storage.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) storage.get(key).result();
    }

    public <T> T getAs(String key, Class<T> clazz) {
        return (T) storage.get(key).result();
    }

    public <T> List<T> getList(String key, Class<T> clazz) {
        return (List<T>) storage.get(key).result();
    }
}
