package com.locadora.filme.application.query;

import com.locadora.shared.cqrs.Query;
import com.locadora.filme.presentation.FilmeResponse;

import java.util.UUID;

/**
 * Query para buscar um filme por ID
 */
public record BuscarFilmePorIdQuery(UUID filmeId) implements Query<FilmeResponse> {
    
    public BuscarFilmePorIdQuery {
        if (filmeId == null) {
            throw new IllegalArgumentException("ID do filme é obrigatório");
        }
    }
}