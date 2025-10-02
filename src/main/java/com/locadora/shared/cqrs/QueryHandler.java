package com.locadora.shared.cqrs;

/**
 * Interface para handlers de queries no padrão CQRS
 */
public interface QueryHandler<T extends Query<R>, R> {
    
    R handle(T query);
}