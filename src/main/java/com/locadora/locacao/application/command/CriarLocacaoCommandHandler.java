package com.locadora.locacao.application.command;

import com.locadora.cliente.domain.ClienteId;
import com.locadora.cliente.domain.ClienteRepository;
import com.locadora.filme.domain.FilmeId;
import com.locadora.filme.domain.FilmeRepository;
import com.locadora.locacao.domain.Locacao;
import com.locadora.locacao.domain.LocacaoRepository;
import com.locadora.shared.cqrs.CommandHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Handler para o comando de criar locação
 */
@Service
@Transactional
public class CriarLocacaoCommandHandler implements CommandHandler<CriarLocacaoCommand, UUID> {
    
    private final LocacaoRepository locacaoRepository;
    private final ClienteRepository clienteRepository;
    private final FilmeRepository filmeRepository;
    
    public CriarLocacaoCommandHandler(
            LocacaoRepository locacaoRepository,
            ClienteRepository clienteRepository,
            FilmeRepository filmeRepository) {
        this.locacaoRepository = locacaoRepository;
        this.clienteRepository = clienteRepository;
        this.filmeRepository = filmeRepository;
    }
    
    @Override
    public UUID handle(CriarLocacaoCommand command) {
        ClienteId clienteId = ClienteId.of(command.clienteId());
        FilmeId filmeId = FilmeId.of(command.filmeId());
        
        // Verifica se o cliente existe
        if (!clienteRepository.existsById(clienteId)) {
            throw new IllegalArgumentException("Cliente não encontrado: " + command.clienteId());
        }
        
        // Verifica se o filme existe
        if (!filmeRepository.existsById(filmeId)) {
            throw new IllegalArgumentException("Filme não encontrado: " + command.filmeId());
        }
        
        Locacao locacao = Locacao.criar(
                clienteId,
                filmeId,
                command.dataLocacao(),
                command.dataPrevistaDevolucao(),
                command.valorLocacao()
        );
        
        locacaoRepository.save(locacao);
        
        return locacao.getLocacaoId().getValue();
    }
}