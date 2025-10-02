package com.locadora.cliente.application.command;

import com.locadora.cliente.domain.Cliente;
import com.locadora.cliente.domain.ClienteRepository;
import com.locadora.shared.cqrs.CommandHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Handler para o comando de criar cliente
 */
@Service
@Transactional
public class CriarClienteCommandHandler implements CommandHandler<CriarClienteCommand, UUID> {
    
    private final ClienteRepository clienteRepository;
    
    public CriarClienteCommandHandler(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }
    
    @Override
    public UUID handle(CriarClienteCommand command) {
        // Verifica se já existe cliente com o mesmo email
        if (clienteRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Já existe um cliente com este email: " + command.email());
        }
        
        // Verifica se já existe cliente com o mesmo CPF
        if (clienteRepository.existsByCpf(command.cpf())) {
            throw new IllegalArgumentException("Já existe um cliente com este CPF: " + command.cpf());
        }
        
        Cliente cliente = Cliente.criar(
                command.nome(),
                command.email(),
                command.telefone(),
                command.cpf()
        );
        
        clienteRepository.save(cliente);
        
        return cliente.getClienteId().getValue();
    }
}