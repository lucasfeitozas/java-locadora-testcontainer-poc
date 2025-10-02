package com.locadora.shared.domain;

import java.time.LocalDateTime;

/**
 * Interface base para eventos de domínio
 */
public interface DomainEvent {
    
    LocalDateTime getOccurredOn();
    
    String getEventType();
}