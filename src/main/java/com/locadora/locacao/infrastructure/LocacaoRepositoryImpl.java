package com.locadora.locacao.infrastructure;

import com.locadora.cliente.domain.ClienteId;
import com.locadora.filme.domain.FilmeId;
import com.locadora.locacao.domain.Locacao;
import com.locadora.locacao.domain.LocacaoId;
import com.locadora.locacao.domain.LocacaoRepository;
import com.locadora.locacao.domain.StatusLocacao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class LocacaoRepositoryImpl implements LocacaoRepository {
    
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final LocacaoRowMapper locacaoRowMapper;
    
    public LocacaoRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.locacaoRowMapper = new LocacaoRowMapper();
    }
    
    @Override
    public void save(Locacao locacao) {
        String sql = """
            INSERT INTO locacao (id, cliente_id, filme_id, data_locacao, data_prevista_devolucao, 
                               data_devolucao, status, valor_locacao, valor_multa, criado_em, atualizado_em)
            VALUES (:id, :clienteId, :filmeId, :dataLocacao, :dataPrevistaDevolucao, 
                   :dataDevolucao, :status::status_locacao, :valorLocacao, :valorMulta, :criadoEm, :atualizadoEm)
            ON CONFLICT (id) DO UPDATE SET
                data_devolucao = EXCLUDED.data_devolucao,
                status = EXCLUDED.status,
                valor_multa = EXCLUDED.valor_multa,
                atualizado_em = EXCLUDED.atualizado_em
            """;
        
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", locacao.getLocacaoId().getValue())
                .addValue("clienteId", locacao.getClienteId().getValue())
                .addValue("filmeId", locacao.getFilmeId().getValue())
                .addValue("dataLocacao", Date.valueOf(locacao.getDataLocacao()))
                .addValue("dataPrevistaDevolucao", Date.valueOf(locacao.getDataPrevistaDevolucao()))
                .addValue("dataDevolucao", locacao.getDataDevolucao() != null ? Date.valueOf(locacao.getDataDevolucao()) : null)
                .addValue("status", locacao.getStatus().name())
                .addValue("valorLocacao", locacao.getValorLocacao())
                .addValue("valorMulta", locacao.getValorMulta())
                .addValue("criadoEm", Timestamp.valueOf(locacao.getCriadoEm()))
                .addValue("atualizadoEm", Timestamp.valueOf(locacao.getAtualizadoEm()));
        
        jdbcTemplate.update(sql, params);
    }
    
    @Override
    public Optional<Locacao> findById(LocacaoId id) {
        String sql = """
            SELECT id, cliente_id, filme_id, data_locacao, data_prevista_devolucao, 
                   data_devolucao, status, valor_locacao, valor_multa, criado_em, atualizado_em
            FROM locacao
            WHERE id = :id
            """;
        
        MapSqlParameterSource params = new MapSqlParameterSource("id", id.getValue());
        
        try {
            Locacao locacao = jdbcTemplate.queryForObject(sql, params, locacaoRowMapper);
            return Optional.of(locacao);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public List<Locacao> findAll() {
        String sql = """
            SELECT id, cliente_id, filme_id, data_locacao, data_prevista_devolucao, 
                   data_devolucao, status, valor_locacao, valor_multa, criado_em, atualizado_em
            FROM locacao
            ORDER BY data_locacao DESC
            """;
        
        return jdbcTemplate.query(sql, locacaoRowMapper);
    }
    
    @Override
    public List<Locacao> findByClienteId(ClienteId clienteId) {
        String sql = """
            SELECT id, cliente_id, filme_id, data_locacao, data_prevista_devolucao, 
                   data_devolucao, status, valor_locacao, valor_multa, criado_em, atualizado_em
            FROM locacao
            WHERE cliente_id = :clienteId
            ORDER BY data_locacao DESC
            """;
        
        MapSqlParameterSource params = new MapSqlParameterSource("clienteId", clienteId.getValue());
        return jdbcTemplate.query(sql, params, locacaoRowMapper);
    }
    
    @Override
    public List<Locacao> findByFilmeId(FilmeId filmeId) {
        String sql = """
            SELECT id, cliente_id, filme_id, data_locacao, data_prevista_devolucao, 
                   data_devolucao, status, valor_locacao, valor_multa, criado_em, atualizado_em
            FROM locacao
            WHERE filme_id = :filmeId
            ORDER BY data_locacao DESC
            """;
        
        MapSqlParameterSource params = new MapSqlParameterSource("filmeId", filmeId.getValue());
        return jdbcTemplate.query(sql, params, locacaoRowMapper);
    }
    
    @Override
    public List<Locacao> findByStatus(StatusLocacao status) {
        String sql = """
            SELECT id, cliente_id, filme_id, data_locacao, data_prevista_devolucao, 
                   data_devolucao, status, valor_locacao, valor_multa, criado_em, atualizado_em
            FROM locacao
            WHERE status = :status::status_locacao
            ORDER BY data_locacao DESC
            """;
        
        MapSqlParameterSource params = new MapSqlParameterSource("status", status.name());
        return jdbcTemplate.query(sql, params, locacaoRowMapper);
    }
    
    @Override
    public List<Locacao> findByClienteIdAndStatus(ClienteId clienteId, StatusLocacao status) {
        String sql = """
            SELECT id, cliente_id, filme_id, data_locacao, data_prevista_devolucao, 
                   data_devolucao, status, valor_locacao, valor_multa, criado_em, atualizado_em
            FROM locacao
            WHERE cliente_id = :clienteId AND status = :status::status_locacao
            ORDER BY data_locacao DESC
            """;
        
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("clienteId", clienteId.getValue())
                .addValue("status", status.name());
        
        return jdbcTemplate.query(sql, params, locacaoRowMapper);
    }
    
    @Override
    public List<Locacao> findByDataLocacao(LocalDate dataLocacao) {
        String sql = """
            SELECT id, cliente_id, filme_id, data_locacao, data_prevista_devolucao, 
                   data_devolucao, status, valor_locacao, valor_multa, criado_em, atualizado_em
            FROM locacao
            WHERE data_locacao = :dataLocacao
            ORDER BY criado_em DESC
            """;
        
        MapSqlParameterSource params = new MapSqlParameterSource("dataLocacao", Date.valueOf(dataLocacao));
        return jdbcTemplate.query(sql, params, locacaoRowMapper);
    }
    
    @Override
    public List<Locacao> findByDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
        String sql = """
            SELECT id, cliente_id, filme_id, data_locacao, data_prevista_devolucao, 
                   data_devolucao, status, valor_locacao, valor_multa, criado_em, atualizado_em
            FROM locacao
            WHERE data_prevista_devolucao = :dataPrevistaDevolucao
            ORDER BY criado_em DESC
            """;
        
        MapSqlParameterSource params = new MapSqlParameterSource("dataPrevistaDevolucao", Date.valueOf(dataPrevistaDevolucao));
        return jdbcTemplate.query(sql, params, locacaoRowMapper);
    }
    
    @Override
    public List<Locacao> findLocacoesAtrasadas() {
        String sql = """
            SELECT id, cliente_id, filme_id, data_locacao, data_prevista_devolucao, 
                   data_devolucao, status, valor_locacao, valor_multa, criado_em, atualizado_em
            FROM locacao
            WHERE status = 'ATIVA'::status_locacao AND data_prevista_devolucao < CURRENT_DATE
            ORDER BY data_prevista_devolucao ASC
            """;
        
        return jdbcTemplate.query(sql, locacaoRowMapper);
    }
    
    @Override
    public void deleteById(LocacaoId id) {
        String sql = "DELETE FROM locacao WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id.getValue());
        jdbcTemplate.update(sql, params);
    }
    
    @Override
    public boolean existsById(LocacaoId id) {
        String sql = "SELECT COUNT(*) FROM locacao WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id.getValue());
        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }
    
    private static class LocacaoRowMapper implements RowMapper<Locacao> {
        @Override
        public Locacao mapRow(ResultSet rs, int rowNum) throws SQLException {
            UUID id = (UUID) rs.getObject("id");
            UUID clienteId = (UUID) rs.getObject("cliente_id");
            UUID filmeId = (UUID) rs.getObject("filme_id");
            LocalDate dataLocacao = rs.getDate("data_locacao").toLocalDate();
            LocalDate dataPrevistaDevolucao = rs.getDate("data_prevista_devolucao").toLocalDate();
            Date dataDevolucaoSql = rs.getDate("data_devolucao");
            LocalDate dataDevolucao = dataDevolucaoSql != null ? dataDevolucaoSql.toLocalDate() : null;
            StatusLocacao status = StatusLocacao.valueOf(rs.getString("status"));
            BigDecimal valorLocacao = rs.getBigDecimal("valor_locacao");
            BigDecimal valorMulta = rs.getBigDecimal("valor_multa");
            Timestamp criadoEm = rs.getTimestamp("criado_em");
            Timestamp atualizadoEm = rs.getTimestamp("atualizado_em");
            
            return new Locacao(
                    LocacaoId.of(id),
                    ClienteId.of(clienteId),
                    FilmeId.of(filmeId),
                    dataLocacao,
                    dataPrevistaDevolucao,
                    dataDevolucao,
                    status,
                    valorLocacao,
                    valorMulta,
                    criadoEm.toLocalDateTime(),
                    atualizadoEm.toLocalDateTime()
            );
        }
    }
}