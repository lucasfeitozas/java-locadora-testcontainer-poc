package com.locadora.filme.presentation;

import com.locadora.filme.application.command.AtualizarFilmeCommand;
import com.locadora.filme.application.command.AtualizarFilmeCommandHandler;
import com.locadora.filme.application.command.CriarFilmeCommand;
import com.locadora.filme.application.command.CriarFilmeCommandHandler;
import com.locadora.filme.application.command.DeletarFilmeCommand;
import com.locadora.filme.application.command.DeletarFilmeCommandHandler;
import com.locadora.filme.application.query.BuscarFilmePorIdQuery;
import com.locadora.filme.application.query.BuscarFilmePorIdQueryHandler;
import com.locadora.filme.application.query.ListarFilmesQuery;
import com.locadora.filme.application.query.ListarFilmesQueryHandler;
import com.locadora.filme.domain.Filme;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller REST para operações CRUD de filmes
 */
@RestController
@RequestMapping("/api/filmes")
@CrossOrigin(origins = "*")
public class FilmeController {
    
    private final CriarFilmeCommandHandler criarFilmeHandler;
    private final AtualizarFilmeCommandHandler atualizarFilmeHandler;
    private final DeletarFilmeCommandHandler deletarFilmeHandler;
    private final BuscarFilmePorIdQueryHandler buscarFilmePorIdHandler;
    private final ListarFilmesQueryHandler listarFilmesHandler;
    
    public FilmeController(
            CriarFilmeCommandHandler criarFilmeHandler,
            AtualizarFilmeCommandHandler atualizarFilmeHandler,
            DeletarFilmeCommandHandler deletarFilmeHandler,
            BuscarFilmePorIdQueryHandler buscarFilmePorIdHandler,
            ListarFilmesQueryHandler listarFilmesHandler) {
        this.criarFilmeHandler = criarFilmeHandler;
        this.atualizarFilmeHandler = atualizarFilmeHandler;
        this.deletarFilmeHandler = deletarFilmeHandler;
        this.buscarFilmePorIdHandler = buscarFilmePorIdHandler;
        this.listarFilmesHandler = listarFilmesHandler;
    }
    
    @PostMapping
    public ResponseEntity<FilmeResponse> criarFilme(@Valid @RequestBody CriarFilmeRequest request) {
        try {
            CriarFilmeCommand command = new CriarFilmeCommand(
                    request.nome(),
                    request.diretor(),
                    request.ano(),
                    request.atores(),
                    request.sinopse(),
                    request.genero(),
                    request.duracao(),
                    request.classificacao(),
                    request.idioma(),
                    request.pais()
            );
            
            UUID filmeId = criarFilmeHandler.handle(command);
            
            // Busca o filme criado para retornar os dados completos
            FilmeResponse filme = buscarFilmePorIdHandler.handle(new BuscarFilmePorIdQuery(filmeId));
            
            return filme != null 
                    ? ResponseEntity.status(HttpStatus.CREATED).body(filme)
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FilmeResponse> buscarFilmePorId(@PathVariable UUID id) {
        try {
            BuscarFilmePorIdQuery query = new BuscarFilmePorIdQuery(id);
            FilmeResponse filme = buscarFilmePorIdHandler.handle(query);
            
            return filme != null 
                    ? ResponseEntity.ok(filme)
                    : ResponseEntity.notFound().build();
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<FilmeResponse>> listarFilmes(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String diretor,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String ator) {
        
        ListarFilmesQuery query = new ListarFilmesQuery(nome, diretor, ano, genero, ator);
        List<FilmeResponse> filmes = listarFilmesHandler.handle(query);
        
        return ResponseEntity.ok(filmes);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<FilmeResponse> atualizarFilme(
            @PathVariable UUID id,
            @Valid @RequestBody AtualizarFilmeRequest request) {
        try {
            AtualizarFilmeCommand command = new AtualizarFilmeCommand(
                    id,
                    request.nome(),
                    request.diretor(),
                    request.ano(),
                    request.atores(),
                    request.sinopse(),
                    request.genero(),
                    request.duracao(),
                    request.classificacao(),
                    request.idioma(),
                    request.pais()
            );
            
            atualizarFilmeHandler.handle(command);
            
            // Busca o filme atualizado para retornar os dados completos
            FilmeResponse filme = buscarFilmePorIdHandler.handle(new BuscarFilmePorIdQuery(id));
            
            return filme != null 
                    ? ResponseEntity.ok(filme)
                    : ResponseEntity.notFound().build();
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFilme(@PathVariable UUID id) {
        try {
            DeletarFilmeCommand command = new DeletarFilmeCommand(id);
            deletarFilmeHandler.handle(command);
            
            return ResponseEntity.noContent().build();
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}