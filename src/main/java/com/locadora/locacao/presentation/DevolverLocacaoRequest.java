package com.locadora.locacao.presentation;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO para requisição de devolução de locação
 */
public record DevolverLocacaoRequest(
        @NotNull(message = "Data de devolução é obrigatória")
        LocalDate dataDevolucao
) {}