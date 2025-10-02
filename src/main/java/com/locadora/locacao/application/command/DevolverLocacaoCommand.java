package com.locadora.locacao.application.command;

import com.locadora.shared.cqrs.Command;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Comando para devolver uma locação
 */
public record DevolverLocacaoCommand(
        UUID locacaoId,
        LocalDate dataDevolucao
) implements Command {
    
    public DevolverLocacaoCommand {
        if (locacaoId == null) {
            throw new IllegalArgumentException("ID da locação é obrigatório");
        }
        if (dataDevolucao == null) {
            throw new IllegalArgumentException("Data de devolução é obrigatória");
        }
    }
}