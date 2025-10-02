package com.locadora.filme.application.command;

import com.locadora.filme.domain.FilmeId;
import com.locadora.filme.domain.FilmeRepository;
import com.locadora.shared.cqrs.CommandHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handler para o comando de deletar filme
 */
@Service
@Transactional
public class DeletarFilmeCommandHandler implements CommandHandler<DeletarFilmeCommand, Void> {
    
    private final FilmeRepository filmeRepository;
    
    public DeletarFilmeCommandHandler(FilmeRepository filmeRepository) {
        this.filmeRepository = filmeRepository;
    }
    
    @Override
    public Void handle(DeletarFilmeCommand command) {
        FilmeId filmeId = FilmeId.of(command.filmeId());
        
        if (!filmeRepository.existsById(filmeId)) {
            throw new IllegalArgumentException("Filme não encontrado: " + command.filmeId());
        }
        
        filmeRepository.deleteById(filmeId);
        
        return null;
    }
}