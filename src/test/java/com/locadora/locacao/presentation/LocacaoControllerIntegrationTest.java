package com.locadora.locacao.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.locadora.config.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

/**
 * Testes de integração para LocacaoController
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LocacaoControllerIntegrationTest extends BaseIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void deveCriarLocacaoComSucesso() throws Exception {
        CriarLocacaoRequest request = new CriarLocacaoRequest(
                UUID.fromString("660e8400-e29b-41d4-a716-446655440001"), // cliente existente
                UUID.fromString("550e8400-e29b-41d4-a716-446655440002"), // filme existente
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                new BigDecimal("20.00")
        );

        String responseContent = mockMvc.perform(post("/api/locacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        System.out.println("Response JSON: " + responseContent);
        
        // Verificar se a resposta contém os campos esperados (usando snake_case devido à configuração Jackson)
        mockMvc.perform(post("/api/locacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cliente_id").value("660e8400-e29b-41d4-a716-446655440001"))
                .andExpect(jsonPath("$.filme_id").value("550e8400-e29b-41d4-a716-446655440002"))
                .andExpect(jsonPath("$.status").value("ATIVA"))
                .andExpect(jsonPath("$.valor_locacao").value(20.00));
    }

    @Test
    void deveBuscarLocacaoPorId() throws Exception {
        mockMvc.perform(get("/api/locacoes/770e8400-e29b-41d4-a716-446655440001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cliente_id").value("660e8400-e29b-41d4-a716-446655440001"))
                .andExpect(jsonPath("$.filme_id").value("550e8400-e29b-41d4-a716-446655440001"))
                .andExpect(jsonPath("$.status").value("ATIVA"))
                .andExpect(jsonPath("$.valor_locacao").value(15.00));
    }

    @Test
    void deveListarLocacoes() throws Exception {
        mockMvc.perform(get("/api/locacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deveListarLocacoesPorCliente() throws Exception {
        mockMvc.perform(get("/api/locacoes")
                        .param("clienteId", "660e8400-e29b-41d4-a716-446655440001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].cliente_id").value("660e8400-e29b-41d4-a716-446655440001"));
    }

    @Test
    void deveListarLocacoesPorStatus() throws Exception {
        mockMvc.perform(get("/api/locacoes")
                        .param("status", "ATIVA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("ATIVA"));
    }

    @Test
    void deveListarLocacoesAtrasadas() throws Exception {
        mockMvc.perform(get("/api/locacoes/atrasadas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deveDevolverLocacao() throws Exception {
        DevolverLocacaoRequest request = new DevolverLocacaoRequest(
                LocalDate.now()
        );

        mockMvc.perform(put("/api/locacoes/770e8400-e29b-41d4-a716-446655440001/devolver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DEVOLVIDA"))
                .andExpect(jsonPath("$.data_devolucao").exists());
    }

    @Test
    void deveDeletarLocacao() throws Exception {
        mockMvc.perform(delete("/api/locacoes/770e8400-e29b-41d4-a716-446655440002"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundParaLocacaoInexistente() throws Exception {
        mockMvc.perform(get("/api/locacoes/770e8400-e29b-41d4-a716-446655440999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveValidarClienteExistenteAoCriarLocacao() throws Exception {
        CriarLocacaoRequest request = new CriarLocacaoRequest(
                UUID.fromString("660e8400-e29b-41d4-a716-446655440999"), // cliente inexistente
                UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                new BigDecimal("20.00")
        );

        mockMvc.perform(post("/api/locacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveValidarFilmeExistenteAoCriarLocacao() throws Exception {
        CriarLocacaoRequest request = new CriarLocacaoRequest(
                UUID.fromString("660e8400-e29b-41d4-a716-446655440001"),
                UUID.fromString("550e8400-e29b-41d4-a716-446655440999"), // filme inexistente
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                new BigDecimal("20.00")
        );

        mockMvc.perform(post("/api/locacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveValidarDataPrevistaDevolucaoAoCriarLocacao() throws Exception {
        CriarLocacaoRequest request = new CriarLocacaoRequest(
                UUID.fromString("660e8400-e29b-41d4-a716-446655440001"),
                UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
                LocalDate.now(),
                LocalDate.now().minusDays(1), // data no passado
                new BigDecimal("20.00")
        );

        mockMvc.perform(post("/api/locacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveCalcularMultaPorAtraso() throws Exception {
        // Criar uma locação com data de devolução no passado
        CriarLocacaoRequest criarRequest = new CriarLocacaoRequest(
                UUID.fromString("660e8400-e29b-41d4-a716-446655440001"),
                UUID.fromString("550e8400-e29b-41d4-a716-446655440002"),
                LocalDate.now().minusDays(10),
                LocalDate.now().minusDays(3), // deveria ter devolvido há 3 dias
                new BigDecimal("10.00")
        );

        String response = mockMvc.perform(post("/api/locacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criarRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extrair o ID da locação criada
        String locacaoId = objectMapper.readTree(response).get("id").asText();

        // Devolver com atraso
        DevolverLocacaoRequest devolverRequest = new DevolverLocacaoRequest(
                LocalDate.now()
        );

        mockMvc.perform(put("/api/locacoes/" + locacaoId + "/devolver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(devolverRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DEVOLVIDA"))
                .andExpect(jsonPath("$.valor_multa").exists())
                .andExpect(jsonPath("$.valor_multa").value(3.00)); // 3 dias * 10% * 10.00 = 3.00
    }
}