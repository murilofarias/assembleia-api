package com.murilofarias.assembleiaapi.domain.usecase;

import com.murilofarias.assembleiaapi.domain.error.DomainException;
import com.murilofarias.assembleiaapi.domain.error.ResourceNotFoundException;
import com.murilofarias.assembleiaapi.domain.model.*;
import com.murilofarias.assembleiaapi.domain.service.message.MessageProducer;
import com.murilofarias.assembleiaapi.infra.repository.AssociadoRepository;
import com.murilofarias.assembleiaapi.infra.repository.PautaRepository;
import com.murilofarias.assembleiaapi.infra.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;


import java.util.NoSuchElementException;

@Component
public class CadastrarVotoUseCase {

    @Autowired
    AssociadoRepository associadoRepository;

    @Autowired
    VotoRepository votoRepository;

    @Autowired
    PautaRepository pautaRepository;

    @Autowired
    ValidarVotoUseCase validarVotoUseCase;

    @Autowired
    ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    MessageProducer producer;

    public Voto execute(Long pautaId, String cpfAssociado, String votoSimbolo){

        Pauta pauta;

        try {
            pauta = pautaRepository.findById(pautaId).orElseThrow();
        }catch(NoSuchElementException ex) {
            throw new ResourceNotFoundException("Nenhuma pauta com id " + pautaId + " foi encontrada!");
        }

        if(!pauta.getStatus().equals(SessaoStatus.ABERTA)){
            throw new DomainException("Erro no cadastro do voto", "Pauta precisa estar aberta!");
        }

        Associado associado;

        try {
            associado = associadoRepository.findByCpf(cpfAssociado).orElseThrow();
        }catch(NoSuchElementException ex) {
            throw new ResourceNotFoundException("Nenhumm associado com cpf " + cpfAssociado + " foi encontrado!");
        }


        if(pauta.getVotos().stream().anyMatch(voto -> voto.getAssociado().getId() == associado.getId())){
            throw new DomainException("Erro no cadastro de voto", "Associado j?? votou nessa pauta!");
        }

        VotoValor votoValor = avaliarVotoValor(votoSimbolo);
        Voto novoVoto = associado.votarPauta(pauta, votoValor);

        novoVoto = votoRepository.save(novoVoto);
        producer.sendVoto(novoVoto);
        return novoVoto;
    }

    private VotoValor avaliarVotoValor(String votoSimbolo){
        if(!votoSimbolo.equalsIgnoreCase("S") && !votoSimbolo.equalsIgnoreCase("N") )
            throw new DomainException("Erro no cadastro de voto", "O voto s?? aceita s??mbolos 'S' e 'N'");

        return votoSimbolo.equalsIgnoreCase("S") ? VotoValor.SIM: VotoValor.NAO;
    }

}