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
    ResultadoSessaoRepository resultadoSessaoRepository;

    public Pauta execute(Long pautaId, Integer duracao){
        Pauta pauta;

        try {
            pauta = pautaRepository.findById(pautaId).orElseThrow();
        }catch(NoSuchElementException ex) {
            throw new ResourceNotFoundException("Nenhuma pauta com id " + pautaId + " foi encontrada!");
        }


        Runnable apurarSessaoUseCase = getApurarSessaoUseCase(pautaId);
        Runnable fecharSessaoUseCase = getFecharSessaoUseCase(pautaId, apurarSessaoUseCase);

        pauta.abrirSessao(duracao);
        pautaRepository.save(pauta);
        taskScheduler.schedule(fecharSessaoUseCase, new Date(System.currentTimeMillis() + 60000L *duracao));
        return pauta;
    }


    @Scheduled
    public Runnable getFecharSessaoUseCase(Long pautaId, Runnable apurarSessaoUseCase){
        return new Runnable() {
            public void run() {
                System.out.println("Task performed on: " + new Date() + "n" +
                        "Thread's name: " + Thread.currentThread().getName());

                Pauta pauta;
                try {
                    pauta = pautaRepository.findById(pautaId).orElseThrow();
                }catch(NoSuchElementException ex) {
                    Thread.currentThread().interrupt();
                    return;
                }

                pauta.fecharSessao();
                pautaRepository.save(pauta);
                taskScheduler.submit(apurarSessaoUseCase);
                System.out.println("Task finished on: " + new Date() + "n" +
                        "Thread's name: " + Thread.currentThread().getName());

                System.out.println("Sessão da pauta com id " + pautaId + " foi fechada");
            }
        };
    }

    @Scheduled
    public Runnable getApurarSessaoUseCase(Long pautaId){
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
