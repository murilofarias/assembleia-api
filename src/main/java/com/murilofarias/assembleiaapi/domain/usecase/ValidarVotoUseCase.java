package com.murilofarias.assembleiaapi.domain.usecase;

import com.murilofarias.assembleiaapi.domain.error.ResourceNotFoundException;
import com.murilofarias.assembleiaapi.domain.model.CpfValidation;
import com.murilofarias.assembleiaapi.domain.model.CpfValidationStatus;
import com.murilofarias.assembleiaapi.domain.model.Pauta;
import com.murilofarias.assembleiaapi.domain.model.Voto;
import com.murilofarias.assembleiaapi.infra.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.NoSuchElementException;

@Component
public class ValidarVotoUseCase {

    @Autowired
    VotoRepository votoRepository;


    public Voto execute(Long votoId, String cpfAssociado){

            Voto voto;
            try {
                voto = votoRepository.findById(votoId).orElseThrow();
            }catch(NoSuchElementException ex) {
                Thread.currentThread().interrupt();
                return null;
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
                return voto;
            }



            if(response != null && response.getStatus().equals(CpfValidationStatus.ABLE_TO_VOTE))
                voto.validarVoto();
            else
                voto.invalidarVoto();

            votoRepository.save(voto);
            return voto;
    }

}