package com.locadora.filme.domain;

import java.util.List;
import java.util.Optional;

/**
 * Interface do repositório para a entidade Filme
 */
public interface FilmeRepository {
    
    void save(Filme filme);
    
    Optional<Filme> findById(FilmeId id);
    
    List<Filme> findAll();
    
    List<Filme> findByNomeContaining(String nome);
    
    List<Filme> findByDiretor(String diretor);
    
    List<Filme> findByAno(Integer ano);
    
    List<Filme> findByGenero(String genero);
    
    List<Filme> findByAtor(String ator);
    
    // Métodos avançados com JSONB
    List<Filme> findByDuracaoMaiorQue(Integer minutos);
    
    List<Filme> findByPais(String pais);
    
    List<Filme> findByClassificacao(String classificacao);
    
    List<Filme> findByMultiplosAtores(List<String> atores);
    
    List<Filme> findBySinopseContendo(String palavra);
    
    void deleteById(FilmeId id);
    
    boolean existsById(FilmeId id);
}