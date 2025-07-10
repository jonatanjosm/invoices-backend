package com.example.invoicesbackend.cqrs;

/**
 * Base interface for all queries in the CQRS pattern.
 * Queries represent read operations that do not change the state of the system.
 * 
 * @param <R> The type of the result returned by the query
 */
public interface Query<R> {
}