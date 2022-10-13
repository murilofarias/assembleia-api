package com.murilofarias.assembleiaapi.domain.usecase;

import com.murilofarias.assembleiaapi.domain.error.ResourceNotFoundException;
import com.murilofarias.assembleiaapi.domain.model.Pauta;
import com.murilofarias.assembleiaapi.domain.model.Voto;
import com.murilofarias.assembleiaapi.infra.PautaRepository;
import com.murilofarias.assembleiaapi.infra.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;


@Component
public class EncontrarVotosUseCase {
    @Autowired
    VotoRepository votoRepository;

    public Page<Voto> execute(long pautaId, Pageable pageable){
        Page<Voto> votos = votoRepository.findByPautaId(pautaId, pageable);
        return votos;
    }
}