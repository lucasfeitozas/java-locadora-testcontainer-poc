package com.locadora.locacao.domain;

import com.locadora.cliente.domain.ClienteId;
import com.locadora.filme.domain.FilmeId;
import com.locadora.shared.domain.DomainEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Evento de domínio disparado quando uma locação é devolvida
 */
public class LocacaoDevolvida implements DomainEvent {
    
    private final LocacaoId locacaoId;
    private final ClienteId clienteId;
    private final FilmeId filmeId;
    private final LocalDate dataDevolucao;
    private final LocalDateTime occurredOn;
    
    public LocacaoDevolvida(LocacaoId locacaoId, ClienteId clienteId, FilmeId filmeId, 
                           LocalDate dataDevolucao, LocalDateTime occurredOn) {
        this.locacaoId = locacaoId;
        this.clienteId = clienteId;
        this.filmeId = filmeId;
        this.dataDevolucao = dataDevolucao;
        this.occurredOn = occurredOn;
    }
    
    public LocacaoId getLocacaoId() {
        return locacaoId;
    }
    
    public ClienteId getClienteId() {
        return clienteId;
    }
    
    public FilmeId getFilmeId() {
        return filmeId;
    }
    
    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }
    
    @Override
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    @Override
    public String getEventType() {
        return "LocacaoDevolvida";
    }
}