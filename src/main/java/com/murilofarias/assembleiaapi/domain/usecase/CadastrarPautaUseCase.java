package com.murilofarias.assembleiaapi.domain.usecase;

import com.murilofarias.assembleiaapi.domain.model.Pauta;
import com.murilofarias.assembleiaapi.infra.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CadastrarPautaUseCase {

    @Autowired
    PautaRepository pautaRepository;

    public Pauta execute(String titulo, String descricao){
        Pauta novaPauta = new Pauta(titulo, descricao);
        return pautaRepository.save(novaPauta);
    }
}
