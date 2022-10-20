package com.murilofarias.assembleiaapi.domain.startup;

import com.murilofarias.assembleiaapi.domain.model.Pauta;
import com.murilofarias.assembleiaapi.domain.model.SessaoStatus;
import com.murilofarias.assembleiaapi.domain.usecase.ApurarSessaoUseCase;
import com.murilofarias.assembleiaapi.domain.usecase.FecharSessaoUseCase;
import com.murilofarias.assembleiaapi.infra.repository.PautaRepository;
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
class ApurarSessaoAgendamento
        implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ApurarSessaoAgendamento.class);

    @Autowired
    PautaRepository pautaRepository;

    @Autowired
    ApurarSessaoUseCase apurarSessaoUseCase;

    @Autowired
    ThreadPoolTaskScheduler taskScheduler;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<Pauta> pautas = pautaRepository.findAllByStatus(SessaoStatus.EM_APURACAO);
        logger.info("Quantidade de pautas em apuração " + pautas.size());
        pautas.forEach(pauta -> taskScheduler.submit(apurarSessaoUseCase.execute(pauta.getId())));

    }

}