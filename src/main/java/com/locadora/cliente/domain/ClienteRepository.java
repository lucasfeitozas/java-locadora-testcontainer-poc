package com.locadora.cliente.domain;

import java.util.List;
import java.util.Optional;

/**
 * Interface do repositório para a entidade Cliente
 */
public interface ClienteRepository {
    
    void save(Cliente cliente);
    
    Optional<Cliente> findById(ClienteId id);
    
    Optional<Cliente> findByEmail(String email);
    
    Optional<Cliente> findByCpf(String cpf);
    
    List<Cliente> findAll();
    
    List<Cliente> findByNomeContaining(String nome);
    
    void deleteById(ClienteId id);
    
    boolean existsById(ClienteId id);
    
    boolean existsByEmail(String email);
    
    boolean existsByCpf(String cpf);
}