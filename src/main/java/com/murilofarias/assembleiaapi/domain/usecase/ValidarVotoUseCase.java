package com.murilofarias.assembleiaapi.domain.usecase;

import com.murilofarias.assembleiaapi.domain.error.ResourceNotFoundException;
import com.murilofarias.assembleiaapi.domain.model.CpfValidation;
import com.murilofarias.assembleiaapi.domain.model.CpfValidationStatus;
import com.murilofarias.assembleiaapi.domain.model.Pauta;
import com.murilofarias.assembleiaapi.domain.model.Voto;
import com.murilofarias.assembleiaapi.infra.PautaRepository;
import com.murilofarias.assembleiaapi.infra.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Date;
import java.util.NoSuchElementException;

@Component
public class ValidarVotoUseCase {

    @Autowired
    VotoRepository votoRepository;


    public Runnable execute(Long votoId, String cpfAssociado){
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
                }catch(Exception e){
                    voto.invalidarVoto();
                    votoRepository.save(voto);
                    Thread.currentThread().interrupt();
                    return;
                }



                if(response != null && response.getStatus().equals(CpfValidationStatus.ABLE_TO_VOTE))
                    voto.validarVoto();
                else
                    voto.invalidarVoto();

                votoRepository.save(voto);
                System.out.println("Voto computado com valor " + voto.getValor().toString());
            }
        };
    }

}