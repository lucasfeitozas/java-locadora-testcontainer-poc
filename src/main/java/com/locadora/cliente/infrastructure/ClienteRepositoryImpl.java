package com.locadora.cliente.infrastructure;

import com.locadora.cliente.domain.Cliente;
import com.locadora.cliente.domain.ClienteId;
import com.locadora.cliente.domain.ClienteRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ClienteRepositoryImpl implements ClienteRepository {
    
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ClienteRowMapper clienteRowMapper;
    
    public ClienteRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.clienteRowMapper = new ClienteRowMapper();
    }
    
    @Override
    public void save(Cliente cliente) {
        String sql = """
            INSERT INTO cliente (id, nome, email, telefone, cpf, criado_em, atualizado_em)
            VALUES (:id, :nome, :email, :telefone, :cpf, :criadoEm, :atualizadoEm)
            ON CONFLICT (id) DO UPDATE SET
                nome = EXCLUDED.nome,
                email = EXCLUDED.email,
                telefone = EXCLUDED.telefone,
                atualizado_em = EXCLUDED.atualizado_em
            """;
        
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", cliente.getClienteId().getValue())
                .addValue("nome", cliente.getNome())
                .addValue("email", cliente.getEmail())
                .addValue("telefone", cliente.getTelefone())
                .addValue("cpf", cliente.getCpf())
                .addValue("criadoEm", Timestamp.valueOf(cliente.getCriadoEm()))
                .addValue("atualizadoEm", Timestamp.valueOf(cliente.getAtualizadoEm()));
        
        jdbcTemplate.update(sql, params);
    }
    
    @Override
    public Optional<Cliente> findById(ClienteId id) {
        String sql = """
            SELECT id, nome, email, telefone, cpf, criado_em, atualizado_em
            FROM cliente
            WHERE id = :id
            """;
        
        MapSqlParameterSource params = new MapSqlParameterSource("id", id.getValue());
        
        try {
            Cliente cliente = jdbcTemplate.queryForObject(sql, params, clienteRowMapper);
            return Optional.of(cliente);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public Optional<Cliente> findByEmail(String email) {
        String sql = """
            SELECT id, nome, email, telefone, cpf, criado_em, atualizado_em
            FROM cliente
            WHERE email = :email
            """;
        
        MapSqlParameterSource params = new MapSqlParameterSource("email", email);
        
        try {
            Cliente cliente = jdbcTemplate.queryForObject(sql, params, clienteRowMapper);
            return Optional.of(cliente);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public Optional<Cliente> findByCpf(String cpf) {
        String sql = """
            SELECT id, nome, email, telefone, cpf, criado_em, atualizado_em
            FROM cliente
            WHERE cpf = :cpf
            """;
        
        MapSqlParameterSource params = new MapSqlParameterSource("cpf", cpf);
        
        try {
            Cliente cliente = jdbcTemplate.queryForObject(sql, params, clienteRowMapper);
            return Optional.of(cliente);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public List<Cliente> findAll() {
        String sql = """
            SELECT id, nome, email, telefone, cpf, criado_em, atualizado_em
            FROM cliente
            ORDER BY nome
            """;
        
        return jdbcTemplate.query(sql, clienteRowMapper);
    }
    
    @Override
    public List<Cliente> findByNomeContaining(String nome) {
        String sql = """
            SELECT id, nome, email, telefone, cpf, criado_em, atualizado_em
            FROM cliente
            WHERE LOWER(nome) LIKE LOWER(:nome)
            ORDER BY nome
            """;
        
        MapSqlParameterSource params = new MapSqlParameterSource("nome", "%" + nome + "%");
        return jdbcTemplate.query(sql, params, clienteRowMapper);
    }
    
    @Override
    public void deleteById(ClienteId id) {
        String sql = "DELETE FROM cliente WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id.getValue());
        jdbcTemplate.update(sql, params);
    }
    
    @Override
    public boolean existsById(ClienteId id) {
        String sql = "SELECT COUNT(*) FROM cliente WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id.getValue());
        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }
    
    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM cliente WHERE email = :email";
        MapSqlParameterSource params = new MapSqlParameterSource("email", email);
        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }
    
    @Override
    public boolean existsByCpf(String cpf) {
        String sql = "SELECT COUNT(*) FROM cliente WHERE cpf = :cpf";
        MapSqlParameterSource params = new MapSqlParameterSource("cpf", cpf);
        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }
    
    private static class ClienteRowMapper implements RowMapper<Cliente> {
        @Override
        public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
            UUID id = (UUID) rs.getObject("id");
            String nome = rs.getString("nome");
            String email = rs.getString("email");
            String telefone = rs.getString("telefone");
            String cpf = rs.getString("cpf");
            Timestamp criadoEm = rs.getTimestamp("criado_em");
            Timestamp atualizadoEm = rs.getTimestamp("atualizado_em");
            
            return new Cliente(
                    ClienteId.of(id),
                    nome,
                    email,
                    telefone,
                    cpf,
                    criadoEm.toLocalDateTime(),
                    atualizadoEm.toLocalDateTime()
            );
        }
    }
}