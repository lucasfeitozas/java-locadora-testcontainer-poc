package com.locadora.cliente.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.locadora.config.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para ClienteController
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClienteControllerIntegrationTest extends BaseIntegrationTest {

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
    void deveCriarClienteComSucesso() throws Exception {
        CriarClienteRequest request = new CriarClienteRequest(
                "Pedro Oliveira",
                "pedro.oliveira@email.com",
                "11777777777",
                "11122233344"
        );

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Pedro Oliveira"))
                .andExpect(jsonPath("$.email").value("pedro.oliveira@email.com"))
                .andExpect(jsonPath("$.cpf").value("11122233344"));
    }

    @Test
    void deveBuscarClientePorId() throws Exception {
        mockMvc.perform(get("/api/clientes/660e8400-e29b-41d4-a716-446655440001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao.silva@email.com"))
                .andExpect(jsonPath("$.cpf").value("12345678901"));
    }

    @Test
    void deveListarClientes() throws Exception {
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deveBuscarClientePorEmail() throws Exception {
        mockMvc.perform(get("/api/clientes/email/joao.silva@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao.silva@email.com"));
    }

    @Test
    void deveBuscarClientePorCpf() throws Exception {
        mockMvc.perform(get("/api/clientes/cpf/12345678901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.cpf").value("12345678901"));
    }

    @Test
    void deveListarClientesPorNome() throws Exception {
        mockMvc.perform(get("/api/clientes")
                        .param("nome", "João"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("João Silva"));
    }

    @Test
    void deveDeletarCliente() throws Exception {
        // Primeiro criar um cliente sem locações
        CriarClienteRequest request = new CriarClienteRequest(
                "Cliente Teste Delete",
                "delete@test.com",
                "11999999999",
                "12345678999"
        );

        String response = mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extrair o ID do cliente criado
        String clienteId = objectMapper.readTree(response).get("id").asText();

        // Agora deletar o cliente
        mockMvc.perform(delete("/api/clientes/" + clienteId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundParaClienteInexistente() throws Exception {
        mockMvc.perform(get("/api/clientes/660e8400-e29b-41d4-a716-446655440999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveValidarEmailDuplicado() throws Exception {
        CriarClienteRequest request = new CriarClienteRequest(
                "Outro João",
                "joao.silva@email.com", // email já existe
                "11666666666",
                "55566677788"
        );

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveValidarCpfDuplicado() throws Exception {
        CriarClienteRequest request = new CriarClienteRequest(
                "Outro João",
                "outro.joao@email.com",
                "11666666666",
                "12345678901" // CPF já existe
        );

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveValidarCamposObrigatoriosAoCriarCliente() throws Exception {
        CriarClienteRequest request = new CriarClienteRequest(
                null, // nome obrigatório
                null, // email obrigatório
                null,
                null  // cpf obrigatório
        );

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}