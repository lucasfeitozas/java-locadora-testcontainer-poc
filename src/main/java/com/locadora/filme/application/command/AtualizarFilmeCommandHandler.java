package com.locadora.filme.application.command;

import com.locadora.filme.domain.Filme;
import com.locadora.filme.domain.FilmeDetalhes;
import com.locadora.filme.domain.FilmeId;
import com.locadora.filme.domain.FilmeRepository;
import com.locadora.shared.cqrs.CommandHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handler para o comando de atualizar filme
 */
@Service
@Transactional
public class AtualizarFilmeCommandHandler implements CommandHandler<AtualizarFilmeCommand, Void> {
    
    private final FilmeRepository filmeRepository;
    
    public AtualizarFilmeCommandHandler(FilmeRepository filmeRepository) {
        this.filmeRepository = filmeRepository;
    }
    
    @Override
    public Void handle(AtualizarFilmeCommand command) {
        FilmeId filmeId = FilmeId.of(command.filmeId());
        
        Filme filme = filmeRepository.findById(filmeId)
                .orElseThrow(() -> new IllegalArgumentException("Filme não encontrado: " + command.filmeId()));
        
        FilmeDetalhes novosDetalhes = new FilmeDetalhes(
                command.atores(),
                command.sinopse(),
                command.genero(),
                command.duracao(),
                command.classificacao(),
                command.idioma(),
                command.pais()
        );
        
        filme.atualizarInformacoes(
                command.nome(),
                command.diretor(),
                command.ano(),
                novosDetalhes
        );
        
        filmeRepository.save(filme);
        
        return null;
    }
}