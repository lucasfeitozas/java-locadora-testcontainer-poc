package com.locadora.filme.application.command;

import com.locadora.shared.cqrs.Command;

import java.util.UUID;

/**
 * Comando para deletar um filme
 */
public record DeletarFilmeCommand(UUID filmeId) implements Command {
    
    public DeletarFilmeCommand {
        if (filmeId == null) {
            throw new IllegalArgumentException("ID do filme é obrigatório");
        }
    }
}