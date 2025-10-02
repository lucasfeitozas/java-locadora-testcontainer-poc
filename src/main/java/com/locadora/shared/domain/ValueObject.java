package com.locadora.shared.domain;

import java.util.Objects;

/**
 * Classe base para Value Objects no DDD
 */
public abstract class ValueObject {
    
    @Override
    public abstract boolean equals(Object obj);
    
    @Override
    public abstract int hashCode();
    
    protected boolean equalsByReflection(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return Objects.equals(this.toString(), obj.toString());
    }
}