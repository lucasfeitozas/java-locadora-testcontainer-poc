package com.locadora.filme.domain;

import com.locadora.shared.domain.DomainEvent;

import java.time.LocalDateTime;

/**
 * Evento de domínio disparado quando um filme é atualizado
 */
public class FilmeAtualizadoEvent implements DomainEvent {
    
    private final FilmeId filmeId;
    private final LocalDateTime occurredOn;
    
    public FilmeAtualizadoEvent(FilmeId filmeId, LocalDateTime occurredOn) {
        this.filmeId = filmeId;
        this.occurredOn = occurredOn;
    }
    
    public FilmeId getFilmeId() {
        return filmeId;
    }
    
    @Override
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    @Override
    public String getEventType() {
        return "FilmeAtualizado";
    }
}