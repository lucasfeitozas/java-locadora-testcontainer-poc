package com.locadora.filme.application.query;

import com.locadora.filme.domain.Filme;
import com.locadora.filme.domain.FilmeId;
import com.locadora.filme.domain.FilmeRepository;
import com.locadora.filme.presentation.FilmeResponse;
import com.locadora.shared.cqrs.QueryHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Handler para a query de buscar filme por ID
 */
@Service
@Transactional(readOnly = true)
public class BuscarFilmePorIdQueryHandler implements QueryHandler<BuscarFilmePorIdQuery, FilmeResponse> {
    
    private final FilmeRepository filmeRepository;
    
    public BuscarFilmePorIdQueryHandler(FilmeRepository filmeRepository) {
        this.filmeRepository = filmeRepository;
    }
    
    @Override
    public FilmeResponse handle(BuscarFilmePorIdQuery query) {
        FilmeId filmeId = FilmeId.of(query.filmeId());
        Optional<Filme> filme = filmeRepository.findById(filmeId);
        return filme.map(FilmeResponse::from).orElse(null);
    }
}