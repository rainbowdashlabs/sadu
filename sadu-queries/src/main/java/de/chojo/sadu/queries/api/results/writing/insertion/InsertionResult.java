package de.chojo.sadu.queries.api.results.writing.insertion;

import de.chojo.sadu.queries.api.results.writing.manipulation.ManipulationResult;

import java.util.List;

public interface InsertionResult extends ManipulationResult {
    List<Long> keys();

}
