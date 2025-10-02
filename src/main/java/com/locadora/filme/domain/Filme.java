package com.locadora.filme.domain;

import com.locadora.shared.domain.AggregateRoot;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agregado Filme - representa um filme na locadora
 */
public class Filme extends AggregateRoot<FilmeId> {
    
    private final FilmeId id;
    private String nome;
    private String diretor;
    private Integer ano;
    private FilmeDetalhes detalhes;
    private final LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    
    public Filme(FilmeId id, String nome, String diretor, Integer ano, FilmeDetalhes detalhes) {
        this.id = Objects.requireNonNull(id, "ID do filme não pode ser nulo");
        this.nome = Objects.requireNonNull(nome, "Nome do filme não pode ser nulo");
        this.diretor = Objects.requireNonNull(diretor, "Diretor não pode ser nulo");
        this.ano = Objects.requireNonNull(ano, "Ano não pode ser nulo");
        this.detalhes = detalhes;
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
        
        validateAno();
        validateNome();
    }
    
    // Construtor para reconstrução a partir do banco de dados
    public Filme(FilmeId id, String nome, String diretor, Integer ano, FilmeDetalhes detalhes, 
                 LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.nome = nome;
        this.diretor = diretor;
        this.ano = ano;
        this.detalhes = detalhes;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }
    
    public static Filme criar(String nome, String diretor, Integer ano, FilmeDetalhes detalhes) {
        return new Filme(FilmeId.generate(), nome, diretor, ano, detalhes);
    }
    
    public void atualizarInformacoes(String nome, String diretor, Integer ano, FilmeDetalhes detalhes) {
        this.nome = Objects.requireNonNull(nome, "Nome do filme não pode ser nulo");
        this.diretor = Objects.requireNonNull(diretor, "Diretor não pode ser nulo");
        this.ano = Objects.requireNonNull(ano, "Ano não pode ser nulo");
        this.detalhes = detalhes;
        this.atualizadoEm = LocalDateTime.now();
        
        validateAno();
        validateNome();
        
        addDomainEvent(new FilmeAtualizadoEvent(this.id, LocalDateTime.now()));
    }
    
    private void validateAno() {
        if (ano < 1888 || ano > LocalDateTime.now().getYear() + 5) {
            throw new IllegalArgumentException("Ano do filme deve estar entre 1888 e " + (LocalDateTime.now().getYear() + 5));
        }
    }
    
    private void validateNome() {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do filme não pode estar vazio");
        }
        if (nome.length() > 255) {
            throw new IllegalArgumentException("Nome do filme não pode ter mais de 255 caracteres");
        }
    }
    
    @Override
    protected FilmeId getId() {
        return id;
    }
    
    public FilmeId getFilmeId() {
        return id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getDiretor() {
        return diretor;
    }
    
    public Integer getAno() {
        return ano;
    }
    
    public FilmeDetalhes getDetalhes() {
        return detalhes;
    }
    
    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
    
    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}