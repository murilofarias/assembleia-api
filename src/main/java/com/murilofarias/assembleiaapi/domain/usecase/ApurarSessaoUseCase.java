package com.murilofarias.assembleiaapi.domain.usecase;

import com.murilofarias.assembleiaapi.domain.model.Pauta;
import com.murilofarias.assembleiaapi.domain.model.ResultadoSessao;
import com.murilofarias.assembleiaapi.infra.PautaRepository;
import com.murilofarias.assembleiaapi.infra.ResultadoSessaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.NoSuchElementException;


@Component
public class ApurarSessaoUseCase {

    @Autowired
    PautaRepository pautaRepository;

    @Autowired
    ResultadoSessaoRepository resultadoSessaoRepository;

    public Runnable execute(Long pautaId){
        return new Runnable() {
            public void run() {
                Pauta pauta;
                try {
                    pauta = pautaRepository.findByIdEager(pautaId).orElseThrow();
                }catch(NoSuchElementException ex) {
                    Thread.currentThread().interrupt();
                    return;
                }

                ResultadoSessao resultadoSessao = new ResultadoSessao(pauta);
                resultadoSessaoRepository.save(resultadoSessao);
                pauta.finalizarApuracao();
                pautaRepository.save(pauta);
                System.out.println("Resultado da secao com id : " + pautaId + "/ S: " + resultadoSessao.getSim() + " N: " + resultadoSessao.getNao());
            }
        };
    }
}