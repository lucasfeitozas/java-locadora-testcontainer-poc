package com.locadora.filme.presentation;

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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para FilmeController
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FilmeControllerIntegrationTest extends BaseIntegrationTest {

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
    void deveCriarFilmeComSucesso() throws Exception {
        CriarFilmeRequest request = new CriarFilmeRequest(
                "Inception",
                "Christopher Nolan",
                2010,
                List.of("Leonardo DiCaprio", "Marion Cotillard"),
                "Um ladrão que invade sonhos",
                "Ficção Científica",
                148,
                "PG-13",
                "Inglês",
                "EUA"
        );

        mockMvc.perform(post("/api/filmes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Inception"))
                .andExpect(jsonPath("$.diretor").value("Christopher Nolan"))
                .andExpect(jsonPath("$.ano").value(2010));
    }

    @Test
    void deveBuscarFilmePorId() throws Exception {
        mockMvc.perform(get("/api/filmes/550e8400-e29b-41d4-a716-446655440001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Matrix"))
                .andExpect(jsonPath("$.diretor").value("Lana Wachowski"))
                .andExpect(jsonPath("$.ano").value(1999));
    }

    @Test
    void deveListarFilmes() throws Exception {
        mockMvc.perform(get("/api/filmes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deveListarFilmesPorGenero() throws Exception {
        mockMvc.perform(get("/api/filmes")
                        .param("genero", "Ficção Científica"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("Matrix"));
    }

    @Test
    void deveAtualizarFilme() throws Exception {
        AtualizarFilmeRequest request = new AtualizarFilmeRequest(
                "Matrix Reloaded",
                "Lana Wachowski",
                2003,
                List.of("Keanu Reeves", "Laurence Fishburne"),
                "Continuação de Matrix",
                "Ficção Científica",
                138,
                "PG-13",
                "Inglês",
                "EUA"
        );

        mockMvc.perform(put("/api/filmes/550e8400-e29b-41d4-a716-446655440001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Matrix Reloaded"))
                .andExpect(jsonPath("$.ano").value(2003));
    }

    @Test
    void deveDeletarFilme() throws Exception {
        // Primeiro criar um filme para deletar
        CriarFilmeRequest request = new CriarFilmeRequest(
                "Filme para Deletar",
                "Diretor Teste",
                2023,
                List.of("Ator Teste"),
                "Filme criado apenas para teste de deleção",
                "Teste",
                120,
                "PG",
                "Português",
                "Brasil"
        );

        String response = mockMvc.perform(post("/api/filmes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extrair o ID do filme criado
        String filmeId = objectMapper.readTree(response).get("id").asText();

        // Agora deletar o filme
        mockMvc.perform(delete("/api/filmes/" + filmeId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundParaFilmeInexistente() throws Exception {
        mockMvc.perform(get("/api/filmes/550e8400-e29b-41d4-a716-446655440999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveValidarCamposObrigatoriosAoCriarFilme() throws Exception {
        CriarFilmeRequest request = new CriarFilmeRequest(
                null, // nome obrigatório
                null, // diretor obrigatório
                null, // ano obrigatório
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        mockMvc.perform(post("/api/filmes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}