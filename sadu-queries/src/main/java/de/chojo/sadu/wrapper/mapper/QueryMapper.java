package de.chojo.sadu.wrapper.mapper;

import de.chojo.sadu.wrapper.util.RowMapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class QueryMapper {
    private static final Map<Class<?>, List<RowMapper<?>>> MAPPER = new HashMap<>();

    private QueryMapper() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    public static void register(RowMapper<?> rowMapper) {
        MAPPER.computeIfAbsent(rowMapper.clazz(), key -> new ArrayList<>()).add(rowMapper);
    }

    private static List<RowMapper<?>> mapper(Class<?> clazz){
        return MAPPER.getOrDefault(clazz, Collections.emptyList());
    }

    public static <T> Optional<RowMapper<?>> wildcard(Class<T> clazz) {
        return mapper(clazz).stream().filter(RowMapper::isWildcard).findAny();
    }


    public static <T> Optional<? extends RowMapper<?>> find(Class<T> clazz, ResultSet set) {
        return mapper(clazz).stream().map(mapper -> Map.entry(mapper, mapper.applicable(set))).filter(e-> e.getValue() == 0)
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .sorted(Collections.reverseOrder())
                .findFirst();
    }
}
