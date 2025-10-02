package com.locadora.filme.infrastructure;

import com.locadora.config.BaseIntegrationTest;
import com.locadora.filme.domain.Filme;
import com.locadora.filme.domain.FilmeDetalhes;
import com.locadora.filme.domain.FilmeId;
import com.locadora.filme.domain.FilmeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de integração para FilmeRepository com exemplos de consultas JSONB
 */
class FilmeRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private FilmeRepository filmeRepository;

    @Test
    void deveBuscarFilmePorId() {
        FilmeId filmeId = FilmeId.of(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"));
        
        Optional<Filme> filme = filmeRepository.findById(filmeId);
        
        assertThat(filme).isPresent();
        assertThat(filme.get().getNome()).isEqualTo("Matrix");
        assertThat(filme.get().getDiretor()).isEqualTo("Lana Wachowski");
    }

    @Test
    void deveListarTodosOsFilmes() {
        List<Filme> filmes = filmeRepository.findAll();
        
        assertThat(filmes).hasSize(2);
        assertThat(filmes)
                .extracting(Filme::getNome)
                .containsExactlyInAnyOrder("Matrix", "Cidade de Deus");
    }

    @Test
    void deveBuscarFilmesPorNome() {
        List<Filme> filmes = filmeRepository.findByNomeContaining("Matrix");
        
        assertThat(filmes).hasSize(1);
        assertThat(filmes.get(0).getNome()).isEqualTo("Matrix");
    }

    @Test
    void deveBuscarFilmesPorDiretor() {
        List<Filme> filmes = filmeRepository.findByDiretor("Fernando Meirelles");
        
        assertThat(filmes).hasSize(1);
        assertThat(filmes.get(0).getNome()).isEqualTo("Cidade de Deus");
    }

    @Test
    void deveBuscarFilmesPorAno() {
        List<Filme> filmes = filmeRepository.findByAno(1999);
        
        assertThat(filmes).hasSize(1);
        assertThat(filmes.get(0).getNome()).isEqualTo("Matrix");
    }

    @Test
    void deveBuscarFilmesPorGenero() {
        List<Filme> filmes = filmeRepository.findByGenero("Drama");
        
        assertThat(filmes).hasSize(1);
        assertThat(filmes.get(0).getNome()).isEqualTo("Cidade de Deus");
    }

    @Test
    void deveBuscarFilmesPorAtor_ConsultaJSONB() {
        // Teste específico para demonstrar consulta JSONB
        List<Filme> filmes = filmeRepository.findByAtor("Keanu Reeves");
        
        assertThat(filmes).hasSize(1);
        assertThat(filmes.get(0).getNome()).isEqualTo("Matrix");
        assertThat(filmes.get(0).getDetalhes().getAtores()).contains("Keanu Reeves");
    }

    @Test
    void deveBuscarFilmesPorAtorParcial_ConsultaJSONB() {
        // Teste para busca parcial em array JSONB
        List<Filme> filmes = filmeRepository.findByAtor("Alexandre");
        
        assertThat(filmes).hasSize(1);
        assertThat(filmes.get(0).getNome()).isEqualTo("Cidade de Deus");
        assertThat(filmes.get(0).getDetalhes().getAtores()).anyMatch(ator -> ator.contains("Alexandre"));
    }

    @Test
    void deveSalvarNovoFilme() {
        FilmeDetalhes detalhes = new FilmeDetalhes(
                List.of("John Travolta", "Samuel L. Jackson", "Uma Thurman"),
                "Histórias entrelaçadas de crime",
                "Crime",
                154,
                "R",
                "Inglês",
                "EUA"
        );
        
        Filme novoFilme = new Filme(
                FilmeId.of(UUID.randomUUID()),
                "Pulp Fiction",
                "Quentin Tarantino",
                1994,
                detalhes
        );

        filmeRepository.save(novoFilme);

        Optional<Filme> filmeSalvo = filmeRepository.findById(novoFilme.getFilmeId());
        assertThat(filmeSalvo).isPresent();
        assertThat(filmeSalvo.get().getNome()).isEqualTo("Pulp Fiction");
        assertThat(filmeSalvo.get().getDetalhes().getAtores()).containsExactly("John Travolta", "Samuel L. Jackson", "Uma Thurman");
    }

    @Test
    void deveAtualizarFilme() {
        FilmeId filmeId = FilmeId.of(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"));
        Optional<Filme> filmeOptional = filmeRepository.findById(filmeId);
        
        assertThat(filmeOptional).isPresent();
        
        Filme filme = filmeOptional.get();
        
        // Criar novos detalhes com atores atualizados
        FilmeDetalhes novosDetalhes = new FilmeDetalhes(
                List.of("Keanu Reeves", "Laurence Fishburne", "Carrie-Anne Moss", "Hugo Weaving"),
                filme.getDetalhes().getSinopse(),
                filme.getDetalhes().getGenero(),
                filme.getDetalhes().getDuracao(),
                filme.getDetalhes().getClassificacao(),
                filme.getDetalhes().getIdioma(),
                filme.getDetalhes().getPais()
        );
        
        filme.atualizarInformacoes("The Matrix", filme.getDiretor(), filme.getAno(), novosDetalhes);
        
        filmeRepository.save(filme);
        
        Optional<Filme> filmeAtualizado = filmeRepository.findById(filmeId);
        assertThat(filmeAtualizado).isPresent();
        assertThat(filmeAtualizado.get().getNome()).isEqualTo("The Matrix");
        assertThat(filmeAtualizado.get().getDetalhes().getAtores()).hasSize(4);
        assertThat(filmeAtualizado.get().getDetalhes().getAtores()).contains("Hugo Weaving");
    }

    @Test
    void deveDeletarFilme() {
        // Criar um novo filme para deletar (sem locações associadas)
        FilmeDetalhes detalhes = new FilmeDetalhes(
                List.of("Actor Test"),
                "Filme para teste de deleção",
                "Teste",
                90,
                "L",
                "Português",
                "Brasil"
        );
        
        Filme filmeParaDeletar = new Filme(
                FilmeId.of(UUID.randomUUID()),
                "Filme Teste Deleção",
                "Diretor Teste",
                2024,
                detalhes
        );

        filmeRepository.save(filmeParaDeletar);
        FilmeId filmeId = filmeParaDeletar.getFilmeId();
        
        // Verificar que o filme existe
        assertThat(filmeRepository.findById(filmeId)).isPresent();
        
        filmeRepository.deleteById(filmeId);
        
        // Verificar que o filme foi deletado
        assertThat(filmeRepository.findById(filmeId)).isEmpty();
    }

    @Test
    void deveVerificarSeFilmeExiste() {
        FilmeId filmeExistente = FilmeId.of(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"));
        FilmeId filmeInexistente = FilmeId.of(UUID.randomUUID());
        
        assertThat(filmeRepository.existsById(filmeExistente)).isTrue();
        assertThat(filmeRepository.existsById(filmeInexistente)).isFalse();
    }

    @Test
    void deveBuscarFilmesComMultiplosCriterios() {
        // Teste combinando múltiplos critérios de busca
        List<Filme> filmes = filmeRepository.findByGenero("Ficção Científica");
        
        assertThat(filmes).hasSize(1);
        
        // Verificar se o filme encontrado também tem o ator específico
        List<Filme> filmesComAtor = filmeRepository.findByAtor("Keanu Reeves");
        assertThat(filmesComAtor).hasSize(1);
        assertThat(filmesComAtor.get(0).getDetalhes().getGenero()).isEqualTo("Ficção Científica");
    }

    @Test
    void deveBuscarFilmesPorDuracaoMaiorQue() {
        // Buscar filmes com duração maior que 120 minutos (ambos Matrix=136 e Cidade de Deus=130)
        List<Filme> filmes = filmeRepository.findByDuracaoMaiorQue(120);
        
        assertThat(filmes).hasSize(2);
        assertThat(filmes).extracting(Filme::getNome)
                .containsExactlyInAnyOrder("Matrix", "Cidade de Deus");
    }

    @Test
    void deveBuscarFilmesPorPais() {
        // Buscar filmes por país
        List<Filme> filmes = filmeRepository.findByPais("EUA");
        
        assertThat(filmes).hasSize(1);
        assertThat(filmes.get(0).getNome()).isEqualTo("Matrix");
    }

    @Test
    void deveBuscarFilmesPorClassificacao() {
        // Buscar filmes por classificação PG-13 (Matrix)
        List<Filme> filmes = filmeRepository.findByClassificacao("PG-13");
        
        assertThat(filmes).hasSize(1);
        assertThat(filmes.get(0).getNome()).isEqualTo("Matrix");
    }

    @Test
    void deveBuscarFilmesPorMultiplosAtores() {
        // Buscar filmes que tenham todos os atores especificados
        List<String> atores = List.of("Keanu Reeves", "Laurence Fishburne");
        List<Filme> filmes = filmeRepository.findByMultiplosAtores(atores);
        
        assertThat(filmes).hasSize(1);
        assertThat(filmes.get(0).getNome()).isEqualTo("Matrix");
    }

    @Test
    void deveBuscarFilmesPorSinopseContendo() {
        // Buscar filmes que contenham uma palavra na sinopse
        List<Filme> filmes = filmeRepository.findBySinopseContendo("realidade");
        
        assertThat(filmes).hasSize(1);
        assertThat(filmes.get(0).getNome()).isEqualTo("Matrix");
    }

    @Test
    void naoDeveEncontrarFilmesComCriteriosInexistentes() {
        // Testes com critérios que não devem retornar resultados
        assertThat(filmeRepository.findByDuracaoMaiorQue(200)).isEmpty();
        assertThat(filmeRepository.findByPais("França")).isEmpty();
        assertThat(filmeRepository.findByClassificacao("R")).isEmpty();
        assertThat(filmeRepository.findByMultiplosAtores(List.of("Ator Inexistente"))).isEmpty();
        assertThat(filmeRepository.findBySinopseContendo("palavra_inexistente")).isEmpty();
    }
}