package com.locadora.locacao.presentation;

import com.locadora.cliente.domain.ClienteId;
import com.locadora.filme.domain.FilmeId;
import com.locadora.locacao.application.command.CriarLocacaoCommand;
import com.locadora.locacao.application.command.CriarLocacaoCommandHandler;
import com.locadora.locacao.application.command.DevolverLocacaoCommand;
import com.locadora.locacao.application.command.DevolverLocacaoCommandHandler;
import com.locadora.locacao.domain.Locacao;
import com.locadora.locacao.domain.LocacaoId;
import com.locadora.locacao.domain.LocacaoRepository;
import com.locadora.locacao.domain.StatusLocacao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller REST para operações CRUD de locações
 */
@RestController
@RequestMapping("/api/locacoes")
@CrossOrigin(origins = "*")
public class LocacaoController {
    
    private final CriarLocacaoCommandHandler criarLocacaoCommandHandler;
    private final DevolverLocacaoCommandHandler devolverLocacaoHandler;
    private final LocacaoRepository locacaoRepository;
    
    public LocacaoController(
            CriarLocacaoCommandHandler criarLocacaoCommandHandler,
            DevolverLocacaoCommandHandler devolverLocacaoHandler,
            LocacaoRepository locacaoRepository) {
        this.criarLocacaoCommandHandler = criarLocacaoCommandHandler;
        this.devolverLocacaoHandler = devolverLocacaoHandler;
        this.locacaoRepository = locacaoRepository;
    }
    
    @PostMapping
    public ResponseEntity<LocacaoResponse> criarLocacao(@Valid @RequestBody CriarLocacaoRequest request) {
        try {
            CriarLocacaoCommand command = new CriarLocacaoCommand(
                    request.clienteId(),
                    request.filmeId(),
                    request.dataLocacao(),
                    request.dataPrevistaDevolucao(),
                    request.valorLocacao()
            );
            
            UUID locacaoIdValue = criarLocacaoCommandHandler.handle(command);
            LocacaoId locacaoId = LocacaoId.of(locacaoIdValue);
            
            // Buscar a locação criada e retornar
            Locacao locacao = locacaoRepository.findById(locacaoId)
                    .orElseThrow(() -> new RuntimeException("Locação não encontrada"));
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(LocacaoResponse.from(locacao));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<LocacaoResponse> buscarLocacaoPorId(@PathVariable UUID id) {
        Optional<Locacao> locacao = locacaoRepository.findById(LocacaoId.of(id));
        
        return locacao.map(l -> ResponseEntity.ok(LocacaoResponse.from(l)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<LocacaoResponse>> listarLocacoes(
            @RequestParam(required = false) UUID clienteId,
            @RequestParam(required = false) UUID filmeId,
            @RequestParam(required = false) StatusLocacao status) {
        
        List<Locacao> locacoes;
        
        if (clienteId != null && status != null) {
            locacoes = locacaoRepository.findByClienteIdAndStatus(ClienteId.of(clienteId), status);
        } else if (clienteId != null) {
            locacoes = locacaoRepository.findByClienteId(ClienteId.of(clienteId));
        } else if (filmeId != null) {
            locacoes = locacaoRepository.findByFilmeId(FilmeId.of(filmeId));
        } else if (status != null) {
            locacoes = locacaoRepository.findByStatus(status);
        } else {
            locacoes = locacaoRepository.findAll();
        }
        
        List<LocacaoResponse> response = locacoes.stream()
                .map(LocacaoResponse::from)
                .toList();
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/atrasadas")
    public ResponseEntity<List<LocacaoResponse>> listarLocacoesAtrasadas() {
        List<Locacao> locacoes = locacaoRepository.findLocacoesAtrasadas();
        
        List<LocacaoResponse> response = locacoes.stream()
                .map(LocacaoResponse::from)
                .toList();
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/devolver")
    public ResponseEntity<LocacaoResponse> devolverLocacao(
            @PathVariable UUID id,
            @Valid @RequestBody DevolverLocacaoRequest request) {
        try {
            DevolverLocacaoCommand command = new DevolverLocacaoCommand(id, request.dataDevolucao());
            devolverLocacaoHandler.handle(command);
            
            // Busca a locação atualizada para retornar os dados completos
            Optional<Locacao> locacao = locacaoRepository.findById(LocacaoId.of(id));
            
            return locacao.map(l -> ResponseEntity.ok(LocacaoResponse.from(l)))
                    .orElse(ResponseEntity.notFound().build());
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLocacao(@PathVariable UUID id) {
        LocacaoId locacaoId = LocacaoId.of(id);
        
        if (!locacaoRepository.existsById(locacaoId)) {
            return ResponseEntity.notFound().build();
        }
        
        locacaoRepository.deleteById(locacaoId);
        return ResponseEntity.noContent().build();
    }
}