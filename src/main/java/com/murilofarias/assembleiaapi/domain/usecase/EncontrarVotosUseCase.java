package com.murilofarias.assembleiaapi.domain.usecase;

import com.murilofarias.assembleiaapi.domain.model.Voto;
import com.murilofarias.assembleiaapi.infra.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


@Component
public class EncontrarVotosUseCase {
    @Autowired
    VotoRepository votoRepository;

    public Page<Voto> execute(long pautaId, Pageable pageable){
        Page<Voto> votos = votoRepository.findByPautaId(pautaId, pageable);
        return votos;
    }
}