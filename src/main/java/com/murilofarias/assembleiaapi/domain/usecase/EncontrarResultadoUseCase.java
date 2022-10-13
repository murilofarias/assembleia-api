package com.murilofarias.assembleiaapi.domain.usecase;

import com.murilofarias.assembleiaapi.domain.error.ResourceNotFoundException;
import com.murilofarias.assembleiaapi.domain.model.ResultadoSessao;
import com.murilofarias.assembleiaapi.infra.repository.ResultadoSessaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;


@Component
public class EncontrarResultadoUseCase {
    @Autowired
    ResultadoSessaoRepository resultadoSessaoRepository;

    public ResultadoSessao execute(long pautaId){
        ResultadoSessao resultadoSessao;

        try {
            resultadoSessao = resultadoSessaoRepository.findByPautaId(pautaId).orElseThrow();
        }catch(NoSuchElementException ex) {
            throw new ResourceNotFoundException("Nenhum resultado com id de pauta " + pautaId + " foi encontrado!");
        }
        return resultadoSessao;
    }
}
