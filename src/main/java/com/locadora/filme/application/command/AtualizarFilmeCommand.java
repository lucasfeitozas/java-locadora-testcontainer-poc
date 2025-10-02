package com.locadora.filme.application.command;

import com.locadora.shared.cqrs.Command;

import java.util.List;
import java.util.UUID;

/**
 * Comando para atualizar um filme existente
 */
public record AtualizarFilmeCommand(
        UUID filmeId,
        String nome,
        String diretor,
        Integer ano,
        List<String> atores,
        String sinopse,
        String genero,
        Integer duracao,
        String classificacao,
        String idioma,
        String pais
) implements Command {
    
    public AtualizarFilmeCommand {
        if (filmeId == null) {
            throw new IllegalArgumentException("ID do filme é obrigatório");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do filme é obrigatório");
        }
        if (diretor == null || diretor.trim().isEmpty()) {
            throw new IllegalArgumentException("Diretor é obrigatório");
        }
        if (ano == null || ano < 1900 || ano > 2030) {
            throw new IllegalArgumentException("Ano deve estar entre 1900 e 2030");
        }
    }
}