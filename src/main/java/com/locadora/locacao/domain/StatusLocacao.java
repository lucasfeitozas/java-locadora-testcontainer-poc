package com.locadora.locacao.domain;

/**
 * Enum para representar o status de uma locação
 */
public enum StatusLocacao {
    ATIVA("Ativa"),
    DEVOLVIDA("Devolvida"),
    ATRASADA("Atrasada"),
    CANCELADA("Cancelada");
    
    private final String descricao;
    
    StatusLocacao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}