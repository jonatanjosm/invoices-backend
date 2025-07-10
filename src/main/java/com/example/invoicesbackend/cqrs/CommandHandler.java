package com.example.invoicesbackend.cqrs;

/**
 * Base interface for all command handlers in the CQRS pattern.
 * Command handlers are responsible for executing commands and producing results.
 * 
 * @param <C> The type of the command to handle
 * @param <R> The type of the result returned by the command
 */
public interface CommandHandler<C extends Command<R>, R> {
    
    /**
     * Handles the given command and returns a result.
     * 
     * @param command The command to handle
     * @return The result of handling the command
     */
    R handle(C command);
}