package com.murilofarias.assembleiaapi.domain.usecase;

import com.murilofarias.assembleiaapi.domain.error.DomainException;
import com.murilofarias.assembleiaapi.domain.error.ResourceNotFoundException;
import com.murilofarias.assembleiaapi.domain.model.*;
import com.murilofarias.assembleiaapi.infra.AssociadoRepository;
import com.murilofarias.assembleiaapi.infra.PautaRepository;
import com.murilofarias.assembleiaapi.infra.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


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
    ThreadPoolTaskScheduler taskScheduler;

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
            throw new DomainException("Erro no cadastro de voto", "Associado já votou nessa pauta!");
        }

        VotoValor votoValor = avaliarVotoValor(votoSimbolo);
        Voto novoVoto = associado.votarPauta(pauta, votoValor);

        novoVoto = votoRepository.save(novoVoto);
        Runnable validacaoVotoUseCase = getValidacaoVotoUseCase(novoVoto.getId(), cpfAssociado);
        taskScheduler.submit(validacaoVotoUseCase);
        return novoVoto;
    }

    private VotoValor avaliarVotoValor(String votoSimbolo){
        if(!votoSimbolo.equalsIgnoreCase("S") && !votoSimbolo.equalsIgnoreCase("N") )
            throw new DomainException("Erro no cadastro de voto", "O voto só aceita símbolos \'S\' e \'N\'");

        return votoSimbolo.equalsIgnoreCase("S") ? VotoValor.SIM: VotoValor.NAO;
    }
    public Runnable getValidacaoVotoUseCase(Long votoId, String cpfAssociado){
        return new Runnable() {
            public void run() {
                Voto voto;
                try {
                    voto = votoRepository.findById(votoId).orElseThrow();
                }catch(NoSuchElementException ex) {
                    throw new ResourceNotFoundException("Nenhum voto com id " + votoId + " foi encontrado!");
                }
                CpfValidation response;

                try {
                    WebClient client = WebClient.create();

                    response = client.get()
                            .uri("https://user-info.herokuapp.com/users/" + cpfAssociado)
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(CpfValidation.class)
                            .block();
                    System.out.println(response.toString());
                    System.out.println(response.getStatus().toString());
                }catch(Exception e){
                    voto.invalidarVoto();
                    votoRepository.save(voto);
                    Thread.currentThread().interrupt();
                    return;
                }


                if(response.getStatus().equals(CpfValidationStatus.ABLE_TO_VOTE))
                    voto.validarVoto();
                else
                    voto.invalidarVoto();

                votoRepository.save(voto);
                System.out.println("Voto computado com valor " + voto.getValor().toString());
            }
        };
    }
}