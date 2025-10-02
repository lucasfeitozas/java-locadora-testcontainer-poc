package com.locadora.cliente.presentation;

import com.locadora.cliente.application.command.CriarClienteCommand;
import com.locadora.cliente.application.command.CriarClienteCommandHandler;
import com.locadora.cliente.domain.Cliente;
import com.locadora.cliente.domain.ClienteId;
import com.locadora.cliente.domain.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller REST para operações CRUD de clientes
 */
@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {
    
    private final CriarClienteCommandHandler criarClienteHandler;
    private final ClienteRepository clienteRepository;
    
    public ClienteController(
            CriarClienteCommandHandler criarClienteHandler,
            ClienteRepository clienteRepository) {
        this.criarClienteHandler = criarClienteHandler;
        this.clienteRepository = clienteRepository;
    }
    
    @PostMapping
    public ResponseEntity<ClienteResponse> criarCliente(@Valid @RequestBody CriarClienteRequest request) {
        try {
            CriarClienteCommand command = new CriarClienteCommand(
                    request.nome(),
                    request.email(),
                    request.telefone(),
                    request.cpf()
            );
            
            UUID clienteId = criarClienteHandler.handle(command);
            
            // Busca o cliente criado para retornar os dados completos
            Optional<Cliente> cliente = clienteRepository.findById(ClienteId.of(clienteId));
            
            return cliente.map(c -> ResponseEntity.status(HttpStatus.CREATED).body(ClienteResponse.from(c)))
                    .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarClientePorId(@PathVariable UUID id) {
        Optional<Cliente> cliente = clienteRepository.findById(ClienteId.of(id));
        
        return cliente.map(c -> ResponseEntity.ok(ClienteResponse.from(c)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listarClientes(
            @RequestParam(required = false) String nome) {
        
        List<Cliente> clientes;
        if (nome != null && !nome.trim().isEmpty()) {
            clientes = clienteRepository.findByNomeContaining(nome);
        } else {
            clientes = clienteRepository.findAll();
        }
        
        List<ClienteResponse> response = clientes.stream()
                .map(ClienteResponse::from)
                .toList();
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteResponse> buscarClientePorEmail(@PathVariable String email) {
        Optional<Cliente> cliente = clienteRepository.findByEmail(email);
        
        return cliente.map(c -> ResponseEntity.ok(ClienteResponse.from(c)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteResponse> buscarClientePorCpf(@PathVariable String cpf) {
        Optional<Cliente> cliente = clienteRepository.findByCpf(cpf);
        
        return cliente.map(c -> ResponseEntity.ok(ClienteResponse.from(c)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable UUID id) {
        ClienteId clienteId = ClienteId.of(id);
        
        if (!clienteRepository.existsById(clienteId)) {
            return ResponseEntity.notFound().build();
        }
        
        clienteRepository.deleteById(clienteId);
        return ResponseEntity.noContent().build();
    }
}