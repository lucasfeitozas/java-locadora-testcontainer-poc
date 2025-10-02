package com.locadora.config;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

/**
 * Classe base para testes de integração
 */
@SpringBootTest
@ActiveProfiles("test")
@Import(TestContainersConfiguration.class)
public abstract class BaseIntegrationTest {
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15-alpine"))
            .withDatabaseName("locadora_test")
            .withUsername("test")
            .withPassword("test");
    
    static {
        postgres.start();
    }
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    
    @BeforeEach
    void setupTestData() {
        // Limpar dados existentes
        jdbcTemplate.execute("DELETE FROM locacao");
        jdbcTemplate.execute("DELETE FROM cliente");
        jdbcTemplate.execute("DELETE FROM filme");
        
        // Inserir dados de teste
        try {
            ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("test-data.sql"));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar dados de teste", e);
        }
    }
}