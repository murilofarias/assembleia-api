package com.murilofarias.assembleiaapi.domain.usecase;

import com.murilofarias.assembleiaapi.domain.model.Pauta;
import com.murilofarias.assembleiaapi.infra.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.NoSuchElementException;


@Component
public class FecharSessaoUseCase {

    @Autowired
    PautaRepository pautaRepository;

    @Autowired
    ApurarSessaoUseCase apurarSessaoUseCase;

    @Autowired
    ThreadPoolTaskScheduler taskScheduler;

    public Runnable execute(Long pautaId){
        return new Runnable() {
            public void run() {

                Pauta pauta;
                try {
                    pauta = pautaRepository.findById(pautaId).orElseThrow();
                }catch(NoSuchElementException ex) {
                    Thread.currentThread().interrupt();
                    return;
                }

                pauta.fecharSessao();
                pautaRepository.save(pauta);
                taskScheduler.schedule(apurarSessaoUseCase.execute(pautaId),  new Date(System.currentTimeMillis() + 15000L));

                System.out.println("Sessão da pauta com id " + pautaId + " foi fechada");
            }
        };
    }

}