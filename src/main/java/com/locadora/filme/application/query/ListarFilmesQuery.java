package com.locadora.filme.application.query;

import com.locadora.shared.cqrs.Query;
import com.locadora.filme.presentation.FilmeResponse;

import java.util.List;

/**
 * Query para listar filmes com filtros opcionais
 */
public record ListarFilmesQuery(
        String nome,
        String diretor,
        Integer ano,
        String genero,
        String ator
) implements Query<List<FilmeResponse>> {
    
    public ListarFilmesQuery() {
        this(null, null, null, null, null);
    }
}