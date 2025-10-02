package com.locadora.locacao.domain;

import com.locadora.cliente.domain.ClienteId;
import com.locadora.filme.domain.FilmeId;
import com.locadora.shared.domain.AggregateRoot;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agregado Locacao - representa uma locação de filme
 */
public class Locacao extends AggregateRoot<LocacaoId> {
    
    private final LocacaoId id;
    private final ClienteId clienteId;
    private final FilmeId filmeId;
    private final LocalDate dataLocacao;
    private final LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao;
    private StatusLocacao status;
    private final BigDecimal valorLocacao;
    private BigDecimal valorMulta;
    private final LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    
    public Locacao(LocacaoId id, ClienteId clienteId, FilmeId filmeId, 
                   LocalDate dataLocacao, LocalDate dataPrevistaDevolucao, BigDecimal valorLocacao) {
        this.id = Objects.requireNonNull(id, "ID da locação não pode ser nulo");
        this.clienteId = Objects.requireNonNull(clienteId, "ID do cliente não pode ser nulo");
        this.filmeId = Objects.requireNonNull(filmeId, "ID do filme não pode ser nulo");
        this.dataLocacao = Objects.requireNonNull(dataLocacao, "Data de locação não pode ser nula");
        this.dataPrevistaDevolucao = Objects.requireNonNull(dataPrevistaDevolucao, "Data prevista de devolução não pode ser nula");
        this.valorLocacao = Objects.requireNonNull(valorLocacao, "Valor da locação não pode ser nulo");
        this.status = StatusLocacao.ATIVA;
        this.valorMulta = BigDecimal.ZERO;
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
        
        validateDatas();
        validateValor();
    }
    
    // Construtor para reconstrução a partir do banco de dados
    public Locacao(LocacaoId id, ClienteId clienteId, FilmeId filmeId, LocalDate dataLocacao,
                   LocalDate dataPrevistaDevolucao, LocalDate dataDevolucao, StatusLocacao status,
                   BigDecimal valorLocacao, BigDecimal valorMulta, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.clienteId = clienteId;
        this.filmeId = filmeId;
        this.dataLocacao = dataLocacao;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.dataDevolucao = dataDevolucao;
        this.status = status;
        this.valorLocacao = valorLocacao;
        this.valorMulta = valorMulta != null ? valorMulta : BigDecimal.ZERO;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }
    
    public static Locacao criar(ClienteId clienteId, FilmeId filmeId, 
                               LocalDate dataLocacao, LocalDate dataPrevistaDevolucao, BigDecimal valorLocacao) {
        return new Locacao(LocacaoId.generate(), clienteId, filmeId, dataLocacao, dataPrevistaDevolucao, valorLocacao);
    }
    
    public void devolver(LocalDate dataDevolucao) {
        if (this.status != StatusLocacao.ATIVA) {
            throw new IllegalStateException("Apenas locações ativas podem ser devolvidas");
        }
        
        this.dataDevolucao = Objects.requireNonNull(dataDevolucao, "Data de devolução não pode ser nula");
        this.status = StatusLocacao.DEVOLVIDA;
        this.atualizadoEm = LocalDateTime.now();
        
        // Calcular multa se houver atraso
        if (dataDevolucao.isAfter(dataPrevistaDevolucao)) {
            long diasAtraso = dataDevolucao.toEpochDay() - dataPrevistaDevolucao.toEpochDay();
            this.valorMulta = valorLocacao.multiply(BigDecimal.valueOf(diasAtraso * 0.1)); // 10% por dia de atraso
        }
        
        addDomainEvent(new LocacaoDevolvida(this.id, this.clienteId, this.filmeId, dataDevolucao, LocalDateTime.now()));
    }
    
    public void marcarComoAtrasada() {
        if (this.status == StatusLocacao.ATIVA && LocalDate.now().isAfter(dataPrevistaDevolucao)) {
            this.status = StatusLocacao.ATRASADA;
            this.atualizadoEm = LocalDateTime.now();
        }
    }
    
    public void cancelar() {
        if (this.status != StatusLocacao.ATIVA) {
            throw new IllegalStateException("Apenas locações ativas podem ser canceladas");
        }
        
        this.status = StatusLocacao.CANCELADA;
        this.atualizadoEm = LocalDateTime.now();
    }
    
    private void validateDatas() {
        if (dataPrevistaDevolucao.isBefore(dataLocacao)) {
            throw new IllegalArgumentException("Data prevista de devolução deve ser posterior à data de locação");
        }
    }
    
    private void validateValor() {
        if (valorLocacao.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da locação deve ser positivo");
        }
    }
    
    @Override
    protected LocacaoId getId() {
        return id;
    }
    
    public LocacaoId getLocacaoId() {
        return id;
    }
    
    public ClienteId getClienteId() {
        return clienteId;
    }
    
    public FilmeId getFilmeId() {
        return filmeId;
    }
    
    public LocalDate getDataLocacao() {
        return dataLocacao;
    }
    
    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }
    
    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }
    
    public StatusLocacao getStatus() {
        return status;
    }
    
    public BigDecimal getValorLocacao() {
        return valorLocacao;
    }
    
    public BigDecimal getValorMulta() {
        return valorMulta;
    }
    
    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
    
    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}