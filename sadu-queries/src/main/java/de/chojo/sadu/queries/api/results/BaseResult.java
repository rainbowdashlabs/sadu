package de.chojo.sadu.queries.api.results;

import java.util.List;

/**
 * The BaseResult interface represents the result of an operation.
 * It provides methods to check if there are any exceptions and retrieve a list of exceptions, if any.
 */
@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface BaseResult {
    /**
     * Returns a boolean value indicating whether there are any exceptions in the result.
     *
     * @return {@code true} if there are any exceptions in the result, {@code false} otherwise.
     */
    default boolean hasExceptions() {
        return !exceptions().isEmpty();
    }

    /**
     * Returns a list of exceptions.
     *
     * @return a list of exceptions. If there are no exceptions, an empty list is returned.
     */
    List<Exception> exceptions();

}
