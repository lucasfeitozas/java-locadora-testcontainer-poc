package com.locadora.cliente.presentation;

import com.locadora.cliente.domain.Cliente;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para resposta de cliente
 */
public record ClienteResponse(
        UUID id,
        String nome,
        String email,
        String telefone,
        String cpf,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
    
    public static ClienteResponse from(Cliente cliente) {
        return new ClienteResponse(
                cliente.getClienteId().getValue(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getCpf(),
                cliente.getCriadoEm(),
                cliente.getAtualizadoEm()
        );
    }
}