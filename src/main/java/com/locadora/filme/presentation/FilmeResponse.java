package com.locadora.filme.presentation;

import com.locadora.filme.domain.Filme;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO para resposta de filme
 */
public record FilmeResponse(
        UUID id,
        String nome,
        String diretor,
        Integer ano,
        List<String> atores,
        String sinopse,
        String genero,
        Integer duracao,
        String classificacao,
        String idioma,
        String pais,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
    
    public static FilmeResponse from(Filme filme) {
        return new FilmeResponse(
                filme.getFilmeId().getValue(),
                filme.getNome(),
                filme.getDiretor(),
                filme.getAno(),
                filme.getDetalhes().getAtores(),
                filme.getDetalhes().getSinopse(),
                filme.getDetalhes().getGenero(),
                filme.getDetalhes().getDuracao(),
                filme.getDetalhes().getClassificacao(),
                filme.getDetalhes().getIdioma(),
                filme.getDetalhes().getPais(),
                filme.getCriadoEm(),
                filme.getAtualizadoEm()
        );
    }
}