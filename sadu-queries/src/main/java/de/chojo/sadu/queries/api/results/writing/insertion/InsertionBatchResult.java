package de.chojo.sadu.queries.api.results.writing.insertion;

import de.chojo.sadu.queries.api.results.writing.manipulation.ManipulationBatchResult;

import java.util.List;

public interface InsertionBatchResult<T extends InsertionResult> extends ManipulationBatchResult<T> {
    List<Long> keys();
}
