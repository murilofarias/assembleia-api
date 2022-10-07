package com.murilofarias.assembleiaapi.domain.usecase;

import com.murilofarias.assembleiaapi.domain.model.Associado;
import com.murilofarias.assembleiaapi.infra.AssociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ListarAssociadosUseCase {
    @Autowired
    AssociadoRepository associadoRepository;

    public Page<Associado> execute(Pageable pageable){
        Page<Associado> associados = associadoRepository.findAll(pageable);
        return associados;
    }
}
