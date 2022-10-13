package com.murilofarias.assembleiaapi.domain.usecase;


import com.murilofarias.assembleiaapi.domain.model.Pauta;
import com.murilofarias.assembleiaapi.infra.repository.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


@Component
public class ListarPautasUseCase {

    @Autowired
    PautaRepository pautaRepository;

    public Page<Pauta> execute(Pageable pageable){
        Page<Pauta> pautas = pautaRepository.findAll(pageable);
        return pautas;
    }
}
