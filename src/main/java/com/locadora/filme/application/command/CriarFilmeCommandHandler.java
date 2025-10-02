package com.locadora.filme.application.command;

import com.locadora.filme.domain.Filme;
import com.locadora.filme.domain.FilmeDetalhes;
import com.locadora.filme.domain.FilmeRepository;
import com.locadora.shared.cqrs.CommandHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Handler para o comando de criar filme
 */
@Service
@Transactional
public class CriarFilmeCommandHandler implements CommandHandler<CriarFilmeCommand, UUID> {
    
    private final FilmeRepository filmeRepository;
    
    public CriarFilmeCommandHandler(FilmeRepository filmeRepository) {
        this.filmeRepository = filmeRepository;
    }
    
    @Override
    public UUID handle(CriarFilmeCommand command) {
        FilmeDetalhes detalhes = new FilmeDetalhes(
                command.atores(),
                command.sinopse(),
                command.genero(),
                command.duracao(),
                command.classificacao(),
                command.idioma(),
                command.pais()
        );
        
        Filme filme = Filme.criar(
                command.nome(),
                command.diretor(),
                command.ano(),
                detalhes
        );
        
        filmeRepository.save(filme);
        
        return filme.getFilmeId().getValue();
    }
}