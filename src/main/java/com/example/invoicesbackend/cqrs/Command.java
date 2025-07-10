package com.example.invoicesbackend.cqrs;

/**
 * Base interface for all commands in the CQRS pattern.
 * Commands represent write operations that change the state of the system.
 * 
 * @param <R> The type of the result returned by the command
 */
public interface Command<R> {
}