package com.locadora.shared.cqrs;

/**
 * Interface para handlers de comandos no padrão CQRS
 */
public interface CommandHandler<T extends Command, R> {
    
    R handle(T command);
}