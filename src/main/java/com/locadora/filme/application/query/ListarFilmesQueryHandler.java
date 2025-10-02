package com.locadora.filme.application.query;

import com.locadora.filme.domain.Filme;
import com.locadora.filme.domain.FilmeRepository;
import com.locadora.filme.presentation.FilmeResponse;
import com.locadora.shared.cqrs.QueryHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Handler para a query de listar filmes
 */
@Service
@Transactional(readOnly = true)
public class ListarFilmesQueryHandler implements QueryHandler<ListarFilmesQuery, List<FilmeResponse>> {
    
    private final FilmeRepository filmeRepository;
    
    public ListarFilmesQueryHandler(FilmeRepository filmeRepository) {
        this.filmeRepository = filmeRepository;
    }
    
    @Override
    public List<FilmeResponse> handle(ListarFilmesQuery query) {
        List<Filme> filmes;
        
        // Se não há filtros, retorna todos os filmes
        if (isQueryEmpty(query)) {
            filmes = filmeRepository.findAll();
        }
        // Aplica filtros específicos
        else if (query.nome() != null && !query.nome().trim().isEmpty()) {
            filmes = filmeRepository.findByNomeContaining(query.nome());
        }
        else if (query.diretor() != null && !query.diretor().trim().isEmpty()) {
            filmes = filmeRepository.findByDiretor(query.diretor());
        }
        else if (query.ano() != null) {
            filmes = filmeRepository.findByAno(query.ano());
        }
        else if (query.genero() != null && !query.genero().trim().isEmpty()) {
            filmes = filmeRepository.findByGenero(query.genero());
        }
        else if (query.ator() != null && !query.ator().trim().isEmpty()) {
            filmes = filmeRepository.findByAtor(query.ator());
        }
        else {
            filmes = filmeRepository.findAll();
        }
        
        return filmes.stream()
                .map(FilmeResponse::from)
                .toList();
    }
    
    private boolean isQueryEmpty(ListarFilmesQuery query) {
        return (query.nome() == null || query.nome().trim().isEmpty()) &&
               (query.diretor() == null || query.diretor().trim().isEmpty()) &&
               query.ano() == null &&
               (query.genero() == null || query.genero().trim().isEmpty()) &&
               (query.ator() == null || query.ator().trim().isEmpty());
    }
}