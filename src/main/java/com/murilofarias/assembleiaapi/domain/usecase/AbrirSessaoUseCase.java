package com.murilofarias.assembleiaapi.domain.usecase;

import com.murilofarias.assembleiaapi.domain.error.ResourceNotFoundException;
import com.murilofarias.assembleiaapi.domain.model.Pauta;
import com.murilofarias.assembleiaapi.domain.model.ResultadoSessao;
import com.murilofarias.assembleiaapi.infra.PautaRepository;
import com.murilofarias.assembleiaapi.infra.ResultadoSessaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.NoSuchElementException;

@Component
public class AbrirSessaoUseCase {

    @Autowired
    PautaRepository pautaRepository;

    @Autowired
    ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    FecharSessaoUseCase fecharSessaoUseCase;


    public Pauta execute(Long pautaId, Integer duracao){
        Pauta pauta;

        try {
            pauta = pautaRepository.findById(pautaId).orElseThrow();
        }catch(NoSuchElementException ex) {
            throw new ResourceNotFoundException("Nenhuma pauta com id " + pautaId + " foi encontrada!");
        }


        pauta.abrirSessao(duracao);
        pautaRepository.save(pauta);
        taskScheduler.schedule(fecharSessaoUseCase.execute(pautaId), new Date(System.currentTimeMillis() + 60000L *duracao));
        return pauta;
    }

}
