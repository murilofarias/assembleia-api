package com.murilofarias.assembleiaapi.domain.startup;

import com.murilofarias.assembleiaapi.domain.model.SessaoStatus;
import com.murilofarias.assembleiaapi.domain.model.Voto;
import com.murilofarias.assembleiaapi.domain.model.VotoStatus;
import com.murilofarias.assembleiaapi.domain.usecase.ValidarVotoUseCase;
import com.murilofarias.assembleiaapi.infra.repository.VotoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Order(1)
class ResolverPendenciaVoto
        implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ResolverPendenciaVoto.class);

    @Autowired
    VotoRepository votoRepository;

    @Autowired
    ValidarVotoUseCase validarVotoUseCase;

    @Autowired
    ThreadPoolTaskScheduler taskScheduler;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<Voto> votos = votoRepository.findAllByStatus(VotoStatus.PENDENTE);
        logger.info("Quantidade de votos pendentes " + votos.size());
        votos.forEach(voto -> {
            if((voto.getPauta().getStatus().equals(SessaoStatus.ABERTA) || voto.getPauta().getStatus().equals(SessaoStatus.EM_APURACAO))){
                taskScheduler.submit(new Runnable() {
                    @Override
                    public void run() {
                        validarVotoUseCase.execute(voto.getId(), voto.getAssociado().getCpf());
                    }
                });
                logger.info("Voto com id" + voto.getId() + " foi submetido a validação");
            }
            else{
                voto.invalidarVoto();
                votoRepository.save(voto);
                logger.info("Voto com id" + voto.getId() + " foi invalidado porque sessão foi realizada");
            }
        });
    }
}