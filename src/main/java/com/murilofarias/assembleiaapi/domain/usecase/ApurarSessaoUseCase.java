package com.murilofarias.assembleiaapi.domain.usecase;

import com.murilofarias.assembleiaapi.domain.model.Pauta;
import com.murilofarias.assembleiaapi.domain.model.ResultadoSessao;
import com.murilofarias.assembleiaapi.domain.model.Voto;
import com.murilofarias.assembleiaapi.domain.model.VotoStatus;
import com.murilofarias.assembleiaapi.domain.service.message.MessageProducer;
import com.murilofarias.assembleiaapi.infra.repository.PautaRepository;
import com.murilofarias.assembleiaapi.infra.repository.ResultadoSessaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Component
public class ApurarSessaoUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ApurarSessaoUseCase.class);

    @Autowired
    PautaRepository pautaRepository;

    @Autowired
    ResultadoSessaoRepository resultadoSessaoRepository;


    @Autowired
    MessageProducer producer;

    @Autowired
    ThreadPoolTaskScheduler taskScheduler;

    public Runnable execute(Long pautaId){
        return new Runnable() {
            public void run() {
                Pauta pauta;
                try {
                    pauta = pautaRepository.findByIdEager(pautaId).orElseThrow();
                }catch(NoSuchElementException ex) {
                    logger.error("Nenhuma pauta com id " + pautaId + " foi encontrada durante a apuração da sua sessão");
                    Thread.currentThread().interrupt();
                    return;
                }

                List<Voto> votosPendentes = pauta.getVotos().stream().filter(voto -> voto.getStatus().equals(VotoStatus.PENDENTE)).collect(Collectors.toList());
                if(!votosPendentes.isEmpty()) {
                    votosPendentes.forEach(voto -> {
                        OffsetDateTime dataLimite = voto.getData().plus(5, ChronoUnit.MINUTES);
                        if(dataLimite.isBefore(OffsetDateTime.now()))
                            producer.sendVoto(voto);
                    });
                    taskScheduler.schedule(this,  new Date(System.currentTimeMillis() + 5* 60000L));
                    logger.info("Pauta com id" + pauta.getId() + " apresenta votos pendentes e teve apuração agendada para daqui a 5 minutos");
                    return;
                }

                ResultadoSessao resultadoSessao = new ResultadoSessao(pauta);
                resultadoSessaoRepository.save(resultadoSessao);
                pauta.finalizarApuracao();
                pautaRepository.save(pauta);
                logger.info("Resultado da secao com id : " + pautaId + "/ S: " + resultadoSessao.getSim() + " N: " + resultadoSessao.getNao());
                producer.sendResultadoSessao(resultadoSessao);
            }
        };
    }
}