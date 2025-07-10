package com.example.invoicesbackend.cqrs;

/**
 * Base interface for all query handlers in the CQRS pattern.
 * Query handlers are responsible for executing queries and producing results.
 * 
 * @param <Q> The type of the query to handle
 * @param <R> The type of the result returned by the query
 */
public interface QueryHandler<Q extends Query<R>, R> {
    
    /**
     * Handles the given query and returns a result.
     * 
     * @param query The query to handle
     * @return The result of handling the query
     */
    R handle(Q query);
}