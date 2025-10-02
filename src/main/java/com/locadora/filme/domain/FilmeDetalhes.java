package com.locadora.filme.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.locadora.shared.domain.ValueObject;

import java.util.List;
import java.util.Objects;

/**
 * Value Object para detalhes do filme (armazenado como JSONB)
 */
public class FilmeDetalhes extends ValueObject {
    
    private final List<String> atores;
    private final String sinopse;
    private final String genero;
    private final Integer duracao; // em minutos
    private final String classificacao;
    private final String idioma;
    private final String pais;
    
    @JsonCreator
    public FilmeDetalhes(
            @JsonProperty("atores") List<String> atores,
            @JsonProperty("sinopse") String sinopse,
            @JsonProperty("genero") String genero,
            @JsonProperty("duracao") Integer duracao,
            @JsonProperty("classificacao") String classificacao,
            @JsonProperty("idioma") String idioma,
            @JsonProperty("pais") String pais) {
        this.atores = atores != null ? List.copyOf(atores) : List.of();
        this.sinopse = sinopse;
        this.genero = genero;
        this.duracao = duracao;
        this.classificacao = classificacao;
        this.idioma = idioma;
        this.pais = pais;
    }
    
    public List<String> getAtores() {
        return atores;
    }
    
    public String getSinopse() {
        return sinopse;
    }
    
    public String getGenero() {
        return genero;
    }
    
    public Integer getDuracao() {
        return duracao;
    }
    
    public String getClassificacao() {
        return classificacao;
    }
    
    public String getIdioma() {
        return idioma;
    }
    
    public String getPais() {
        return pais;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FilmeDetalhes that = (FilmeDetalhes) obj;
        return Objects.equals(atores, that.atores) &&
               Objects.equals(sinopse, that.sinopse) &&
               Objects.equals(genero, that.genero) &&
               Objects.equals(duracao, that.duracao) &&
               Objects.equals(classificacao, that.classificacao) &&
               Objects.equals(idioma, that.idioma) &&
               Objects.equals(pais, that.pais);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(atores, sinopse, genero, duracao, classificacao, idioma, pais);
    }
    
    @Override
    public String toString() {
        return "FilmeDetalhes{" +
               "atores=" + atores +
               ", sinopse='" + sinopse + '\'' +
               ", genero='" + genero + '\'' +
               ", duracao=" + duracao +
               ", classificacao='" + classificacao + '\'' +
               ", idioma='" + idioma + '\'' +
               ", pais='" + pais + '\'' +
               '}';
    }
}