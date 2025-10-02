package com.locadora.cliente.application.command;

import com.locadora.shared.cqrs.Command;

/**
 * Comando para criar um novo cliente
 */
public record CriarClienteCommand(
        String nome,
        String email,
        String telefone,
        String cpf
) implements Command {
    
    public CriarClienteCommand {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }
    }
}