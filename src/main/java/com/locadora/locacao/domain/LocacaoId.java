package com.locadora.locacao.domain;

import com.locadora.shared.domain.ValueObject;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object para identificação de locações
 */
public class LocacaoId extends ValueObject {
    
    private final UUID value;
    
    private LocacaoId(UUID value) {
        this.value = Objects.requireNonNull(value, "LocacaoId não pode ser nulo");
    }
    
    public static LocacaoId of(UUID value) {
        return new LocacaoId(value);
    }
    
    public static LocacaoId of(String value) {
        return new LocacaoId(UUID.fromString(value));
    }
    
    public static LocacaoId generate() {
        return new LocacaoId(UUID.randomUUID());
    }
    
    public UUID getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LocacaoId locacaoId = (LocacaoId) obj;
        return Objects.equals(value, locacaoId.value);
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