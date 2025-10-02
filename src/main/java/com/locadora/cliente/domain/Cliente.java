package com.locadora.cliente.domain;

import com.locadora.shared.domain.AggregateRoot;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agregado Cliente - representa um cliente da locadora
 */
public class Cliente extends AggregateRoot<ClienteId> {
    
    private final ClienteId id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private final LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    
    public Cliente(ClienteId id, String nome, String email, String telefone, String cpf) {
        this.id = Objects.requireNonNull(id, "ID do cliente não pode ser nulo");
        this.nome = Objects.requireNonNull(nome, "Nome do cliente não pode ser nulo");
        this.email = Objects.requireNonNull(email, "Email não pode ser nulo");
        this.telefone = telefone;
        this.cpf = Objects.requireNonNull(cpf, "CPF não pode ser nulo");
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
        
        validateEmail();
        validateCpf();
    }
    
    // Construtor para reconstrução a partir do banco de dados
    public Cliente(ClienteId id, String nome, String email, String telefone, String cpf,
                   LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }
    
    public static Cliente criar(String nome, String email, String telefone, String cpf) {
        return new Cliente(ClienteId.generate(), nome, email, telefone, cpf);
    }
    
    public void atualizarInformacoes(String nome, String email, String telefone) {
        this.nome = Objects.requireNonNull(nome, "Nome do cliente não pode ser nulo");
        this.email = Objects.requireNonNull(email, "Email não pode ser nulo");
        this.telefone = telefone;
        this.atualizadoEm = LocalDateTime.now();
        
        validateEmail();
    }
    
    private void validateEmail() {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email deve ser válido");
        }
    }
    
    private void validateCpf() {
        if (cpf == null || cpf.replaceAll("[^0-9]", "").length() != 11) {
            throw new IllegalArgumentException("CPF deve ter 11 dígitos");
        }
    }
    
    @Override
    protected ClienteId getId() {
        return id;
    }
    
    public ClienteId getClienteId() {
        return id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
    
    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}