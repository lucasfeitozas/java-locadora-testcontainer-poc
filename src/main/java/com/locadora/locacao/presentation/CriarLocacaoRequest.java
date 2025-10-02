package com.locadora.locacao.presentation;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO para requisição de criação de locação
 */
public record CriarLocacaoRequest(
        @NotNull(message = "ID do cliente é obrigatório")
        UUID clienteId,
        
        @NotNull(message = "ID do filme é obrigatório")
        UUID filmeId,
        
        @NotNull(message = "Data de locação é obrigatória")
        LocalDate dataLocacao,
        
        @NotNull(message = "Data prevista de devolução é obrigatória")
        LocalDate dataPrevistaDevolucao,
        
        @NotNull(message = "Valor da locação é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor da locação deve ser maior que zero")
        BigDecimal valorLocacao
) {}