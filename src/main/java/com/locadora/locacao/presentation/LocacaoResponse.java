package com.locadora.locacao.presentation;

import com.locadora.locacao.domain.Locacao;
import com.locadora.locacao.domain.StatusLocacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para resposta de locação
 */
public record LocacaoResponse(
        UUID id,
        UUID clienteId,
        UUID filmeId,
        LocalDate dataLocacao,
        LocalDate dataPrevistaDevolucao,
        LocalDate dataDevolucao,
        StatusLocacao status,
        BigDecimal valorLocacao,
        BigDecimal valorMulta,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
    
    public static LocacaoResponse from(Locacao locacao) {
        return new LocacaoResponse(
                locacao.getLocacaoId().getValue(),
                locacao.getClienteId().getValue(),
                locacao.getFilmeId().getValue(),
                locacao.getDataLocacao(),
                locacao.getDataPrevistaDevolucao(),
                locacao.getDataDevolucao(),
                locacao.getStatus(),
                locacao.getValorLocacao(),
                locacao.getValorMulta(),
                locacao.getCriadoEm(),
                locacao.getAtualizadoEm()
        );
    }
}