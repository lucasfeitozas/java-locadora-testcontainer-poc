package com.locadora.cliente.presentation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para requisição de criação de cliente
 */
public record CriarClienteRequest(
        @NotBlank(message = "Nome é obrigatório")
        String nome,
        
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ter formato válido")
        String email,
        
        String telefone,
        
        @NotBlank(message = "CPF é obrigatório")
        String cpf
) {}