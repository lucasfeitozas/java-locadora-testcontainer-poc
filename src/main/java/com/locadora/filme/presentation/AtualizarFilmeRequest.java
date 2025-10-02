package com.locadora.filme.presentation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * DTO para requisição de atualização de filme
 */
public record AtualizarFilmeRequest(
        @NotBlank(message = "Nome é obrigatório")
        String nome,
        
        @NotBlank(message = "Diretor é obrigatório")
        String diretor,
        
        @NotNull(message = "Ano é obrigatório")
        @Min(value = 1900, message = "Ano deve ser maior ou igual a 1900")
        @Max(value = 2030, message = "Ano deve ser menor ou igual a 2030")
        Integer ano,
        
        List<String> atores,
        String sinopse,
        String genero,
        
        @Min(value = 1, message = "Duração deve ser maior que 0")
        Integer duracao,
        
        String classificacao,
        String idioma,
        String pais
) {}