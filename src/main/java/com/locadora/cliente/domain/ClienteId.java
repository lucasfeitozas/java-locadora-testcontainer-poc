package com.locadora.cliente.domain;

import com.locadora.shared.domain.ValueObject;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object para identificação de clientes
 */
public class ClienteId extends ValueObject {
    
    private final UUID value;
    
    private ClienteId(UUID value) {
        this.value = Objects.requireNonNull(value, "ClienteId não pode ser nulo");
    }
    
    public static ClienteId of(UUID value) {
        return new ClienteId(value);
    }
    
    public static ClienteId of(String value) {
        return new ClienteId(UUID.fromString(value));
    }
    
    public static ClienteId generate() {
        return new ClienteId(UUID.randomUUID());
    }
    
    public UUID getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ClienteId clienteId = (ClienteId) obj;
        return Objects.equals(value, clienteId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}