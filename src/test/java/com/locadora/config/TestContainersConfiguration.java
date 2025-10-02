package com.locadora.config;

import org.springframework.boot.test.context.TestConfiguration;

/**
 * Configuração de Testcontainers para testes de integração
 */
@TestConfiguration(proxyBeanMethods = false)
public class TestContainersConfiguration {
    // Configuração movida para BaseIntegrationTest
}