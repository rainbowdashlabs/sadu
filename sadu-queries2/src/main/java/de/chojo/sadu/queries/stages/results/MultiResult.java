package de.chojo.sadu.queries.stages.results;

import java.util.List;

public class MultiResult<T> {
    private final T results;

    public MultiResult(T results) {
        this.results = results;
    }
}
