package com.locadora.locacao.domain;

import com.locadora.cliente.domain.ClienteId;
import com.locadora.filme.domain.FilmeId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface do repositório para a entidade Locacao
 */
public interface LocacaoRepository {
    
    void save(Locacao locacao);
    
    Optional<Locacao> findById(LocacaoId id);
    
    List<Locacao> findAll();
    
    List<Locacao> findByClienteId(ClienteId clienteId);
    
    List<Locacao> findByFilmeId(FilmeId filmeId);
    
    List<Locacao> findByStatus(StatusLocacao status);
    
    List<Locacao> findByClienteIdAndStatus(ClienteId clienteId, StatusLocacao status);
    
    List<Locacao> findByDataLocacao(LocalDate dataLocacao);
    
    List<Locacao> findByDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao);
    
    List<Locacao> findLocacoesAtrasadas();
    
    void deleteById(LocacaoId id);
    
    boolean existsById(LocacaoId id);
}