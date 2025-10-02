package com.locadora.filme.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locadora.filme.domain.Filme;
import com.locadora.filme.domain.FilmeDetalhes;
import com.locadora.filme.domain.FilmeId;
import com.locadora.filme.domain.FilmeRepository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FilmeRepositoryImpl implements FilmeRepository {
    
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    private final FilmeRowMapper filmeRowMapper;
    
    public FilmeRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
        this.filmeRowMapper = new FilmeRowMapper();
    }
    
    @Override
    public void save(Filme filme) {
        String sql = """
            INSERT INTO filme (id, nome, diretor, ano, detalhes, criado_em, atualizado_em)
            VALUES (:id, :nome, :diretor, :ano, :detalhes::jsonb, :criadoEm, :atualizadoEm)
            ON CONFLICT (id) DO UPDATE SET
                nome = EXCLUDED.nome,
                diretor = EXCLUDED.diretor,
                ano = EXCLUDED.ano,
                detalhes = EXCLUDED.detalhes,
                atualizado_em = EXCLUDED.atualizado_em
            """;
        
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", filme.getFilmeId().getValue())
                .addValue("nome", filme.getNome())
                .addValue("diretor", filme.getDiretor())
                .addValue("ano", filme.getAno())
                .addValue("detalhes", toJsonString(filme.getDetalhes()))
                .addValue("criadoEm", Timestamp.valueOf(filme.getCriadoEm()))
                .addValue("atualizadoEm", Timestamp.valueOf(filme.getAtualizadoEm()));
        
        jdbcTemplate.update(sql, params);
    }
    
    @Override
    public Optional<Filme> findById(FilmeId id) {
        String sql = """
            SELECT id, nome, diretor, ano, detalhes, criado_em, atualizado_em
            FROM filme
            WHERE id = :id
            """;
        
        MapSqlParameterSource params = new MapSqlParameterSource("id", id.getValue());
        
        try {
            Filme filme = jdbcTemplate.queryForObject(sql, params, filmeRowMapper);
            return Optional.of(filme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public List<Filme> findAll() {
        String sql = """
            SELECT id, nome, diretor, ano, detalhes, criado_em, atualizado_em
            FROM filme
            ORDER BY nome
            """;
        
        return jdbcTemplate.query(sql, filmeRowMapper);
    }
    
    @Override
    public List<Filme> findByNomeContaining(String nome) {
        String sql = """
            SELECT id, nome, diretor, ano, detalhes, criado_em, atualizado_em
            FROM filme
            WHERE LOWER(nome) LIKE LOWER(:nome)
            ORDER BY nome
            """;
        
        MapSqlParameterSource params = new MapSqlParameterSource("nome", "%" + nome + "%");
        return jdbcTemplate.query(sql, params, filmeRowMapper);
    }
    
    @Override
    public List<Filme> findByDiretor(String diretor) {
        String sql = """
            SELECT id, nome, diretor, ano, detalhes, criado_em, atualizado_em
            FROM filme
            WHERE LOWER(diretor) = LOWER(:diretor)
            ORDER BY ano DESC
            """;
        
        MapSqlParameterSource params = new MapSqlParameterSource("diretor", diretor);
        return jdbcTemplate.query(sql, params, filmeRowMapper);
    }
    
    @Override
    public List<Filme> findByAno(Integer ano) {
        String sql = """
            SELECT id, nome, diretor, ano, detalhes, criado_em, atualizado_em
            FROM filme
            WHERE ano = :ano
            ORDER BY nome
            """;
        
        MapSqlParameterSource params = new MapSqlParameterSource("ano", ano);
        return jdbcTemplate.query(sql, params, filmeRowMapper);
    }
    
    @Override
    public List<Filme> findByGenero(String genero) {
        String sql = """
            SELECT id, nome, diretor, ano, detalhes, criado_em, atualizado_em
            FROM filme
            WHERE detalhes->>'genero' = :genero
            ORDER BY nome
            """;
        
        MapSqlParameterSource params = new MapSqlParameterSource("genero", genero);
        return jdbcTemplate.query(sql, params, filmeRowMapper);
    }
    
    @Override
    public List<Filme> findByAtor(String ator) {
        String sql = """
            SELECT id, nome, diretor, ano, detalhes, criado_em, atualizado_em
            FROM filme
            WHERE EXISTS (
                SELECT 1 FROM jsonb_array_elements_text(detalhes->'atores') AS ator_nome
                WHERE LOWER(ator_nome) LIKE LOWER(:ator)
            )
            ORDER BY nome
            """;
        
        MapSqlParameterSource params = new MapSqlParameterSource("ator", "%" + ator + "%");
        return jdbcTemplate.query(sql, params, filmeRowMapper);
    }
    
    // Métodos avançados com JSONB
    @Override
    public List<Filme> findByDuracaoMaiorQue(Integer minutos) {
        String sql = """
            SELECT id, nome, diretor, ano, detalhes, criado_em, atualizado_em
            FROM filme
            WHERE (detalhes->>'duracao')::int > :minutos
            ORDER BY (detalhes->>'duracao')::int DESC
            """;
        
        return jdbcTemplate.query(sql, new MapSqlParameterSource("minutos", minutos), filmeRowMapper);
    }
    
    @Override
    public List<Filme> findByPais(String pais) {
        String sql = """
            SELECT id, nome, diretor, ano, detalhes, criado_em, atualizado_em
            FROM filme
            WHERE detalhes->>'pais' = :pais
            ORDER BY nome
            """;
        
        return jdbcTemplate.query(sql, new MapSqlParameterSource("pais", pais), filmeRowMapper);
    }
    
    @Override
    public List<Filme> findByClassificacao(String classificacao) {
        String sql = """
            SELECT id, nome, diretor, ano, detalhes, criado_em, atualizado_em
            FROM filme
            WHERE detalhes->>'classificacao' = :classificacao
            ORDER BY nome
            """;
        
        return jdbcTemplate.query(sql, new MapSqlParameterSource("classificacao", classificacao), filmeRowMapper);
    }
    
    @Override
    public List<Filme> findByMultiplosAtores(List<String> atores) {
        if (atores.isEmpty()) {
            return List.of();
        }
        
        StringBuilder sql = new StringBuilder("""
            SELECT id, nome, diretor, ano, detalhes, criado_em, atualizado_em
            FROM filme
            WHERE 
            """);
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        for (int i = 0; i < atores.size(); i++) {
            if (i > 0) {
                sql.append(" AND ");
            }
            sql.append("jsonb_exists(detalhes->'atores', :ator").append(i).append(")");
            params.addValue("ator" + i, atores.get(i));
        }
        
        sql.append(" ORDER BY nome");
        
        return jdbcTemplate.query(sql.toString(), params, filmeRowMapper);
    }
    
    @Override
    public List<Filme> findBySinopseContendo(String palavra) {
        String sql = """
            SELECT id, nome, diretor, ano, detalhes, criado_em, atualizado_em
            FROM filme
            WHERE detalhes->>'sinopse' ILIKE :palavra
            ORDER BY nome
            """;
        
        return jdbcTemplate.query(sql, new MapSqlParameterSource("palavra", "%" + palavra + "%"), filmeRowMapper);
    }

    @Override
    public void deleteById(FilmeId id) {
        String sql = "DELETE FROM filme WHERE id = :id";
        jdbcTemplate.update(sql, new MapSqlParameterSource("id", id.getValue()));
    }
    
    @Override
    public boolean existsById(FilmeId id) {
        String sql = "SELECT COUNT(*) FROM filme WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id.getValue());
        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }
    
    private String toJsonString(FilmeDetalhes detalhes) {
        if (detalhes == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(detalhes);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar detalhes do filme", e);
        }
    }
    
    private FilmeDetalhes fromJsonString(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, FilmeDetalhes.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao deserializar detalhes do filme", e);
        }
    }
    
    private class FilmeRowMapper implements RowMapper<Filme> {
        @Override
        public Filme mapRow(ResultSet rs, int rowNum) throws SQLException {
            UUID id = (UUID) rs.getObject("id");
            String nome = rs.getString("nome");
            String diretor = rs.getString("diretor");
            Integer ano = rs.getInt("ano");
            
            // Processar JSONB
            String detalhesJson = rs.getString("detalhes");
            FilmeDetalhes detalhes = null;
            if (detalhesJson != null) {
                detalhes = fromJsonString(detalhesJson);
            }
            
            Timestamp criadoEm = rs.getTimestamp("criado_em");
            Timestamp atualizadoEm = rs.getTimestamp("atualizado_em");
            
            return new Filme(
                    FilmeId.of(id),
                    nome,
                    diretor,
                    ano,
                    detalhes,
                    criadoEm.toLocalDateTime(),
                    atualizadoEm.toLocalDateTime()
            );
        }
    }
}