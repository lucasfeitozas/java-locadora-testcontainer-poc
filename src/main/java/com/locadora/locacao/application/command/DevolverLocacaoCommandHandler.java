package com.locadora.locacao.application.command;

import com.locadora.locacao.domain.Locacao;
import com.locadora.locacao.domain.LocacaoId;
import com.locadora.locacao.domain.LocacaoRepository;
import com.locadora.shared.cqrs.CommandHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

/**
 * Handler para o comando de devolver locação
 */
@Service
@Transactional
public class DevolverLocacaoCommandHandler implements CommandHandler<DevolverLocacaoCommand, Void> {
    
    private final LocacaoRepository locacaoRepository;
    
    public DevolverLocacaoCommandHandler(LocacaoRepository locacaoRepository) {
        this.locacaoRepository = locacaoRepository;
    }
    
    @Override
    public Void handle(DevolverLocacaoCommand command) {
        LocacaoId locacaoId = LocacaoId.of(command.locacaoId());
        
        Locacao locacao = locacaoRepository.findById(locacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Locação não encontrada: " + command.locacaoId()));
        
        // Calcula multa se houver atraso
        BigDecimal multa = BigDecimal.ZERO;
        locacao.devolver(command.dataDevolucao());
        
        locacaoRepository.save(locacao);
        
        return null;
    }
}