package com.locadora.filme.domain;

import com.locadora.shared.domain.ValueObject;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object para identificação de filmes
 */
public class FilmeId extends ValueObject {
    
    private final UUID value;
    
    private FilmeId(UUID value) {
        this.value = Objects.requireNonNull(value, "FilmeId não pode ser nulo");
    }
    
    public static FilmeId of(UUID value) {
        return new FilmeId(value);
    }
    
    public static FilmeId of(String value) {
        return new FilmeId(UUID.fromString(value));
    }
    
    public static FilmeId generate() {
        return new FilmeId(UUID.randomUUID());
    }
    
    public UUID getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FilmeId filmeId = (FilmeId) obj;
        return Objects.equals(value, filmeId.value);
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