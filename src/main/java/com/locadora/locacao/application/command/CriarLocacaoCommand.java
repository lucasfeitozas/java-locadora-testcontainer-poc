package com.locadora.locacao.application.command;

import com.locadora.shared.cqrs.Command;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Comando para criar uma nova locação
 */
public record CriarLocacaoCommand(
        UUID clienteId,
        UUID filmeId,
        LocalDate dataLocacao,
        LocalDate dataPrevistaDevolucao,
        BigDecimal valorLocacao
) implements Command {
    
    public CriarLocacaoCommand {
        if (clienteId == null) {
            throw new IllegalArgumentException("ID do cliente é obrigatório");
        }
        if (filmeId == null) {
            throw new IllegalArgumentException("ID do filme é obrigatório");
        }
        if (dataLocacao == null) {
            throw new IllegalArgumentException("Data de locação é obrigatória");
        }
        if (dataPrevistaDevolucao == null) {
            throw new IllegalArgumentException("Data prevista de devolução é obrigatória");
        }
        if (valorLocacao == null || valorLocacao.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da locação deve ser maior que zero");
        }
        if (dataPrevistaDevolucao.isBefore(dataLocacao)) {
            throw new IllegalArgumentException("Data prevista de devolução deve ser posterior à data de locação");
        }
    }
}