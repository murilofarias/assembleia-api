package com.murilofarias.assembleiaapi.domain.usecase;

import com.murilofarias.assembleiaapi.domain.model.Pauta;
import com.murilofarias.assembleiaapi.domain.model.ResultadoSessao;
import com.murilofarias.assembleiaapi.domain.service.message.MessageProducer;
import com.murilofarias.assembleiaapi.infra.repository.PautaRepository;
import com.murilofarias.assembleiaapi.infra.repository.ResultadoSessaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;


@Component
public class ApurarSessaoUseCase {

    @Autowired
    PautaRepository pautaRepository;

    @Autowired
    ResultadoSessaoRepository resultadoSessaoRepository;

    @Autowired
    MessageProducer producer;

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
                producer.sendResultadoSessao(resultadoSessao);
            }
        };
    }
}