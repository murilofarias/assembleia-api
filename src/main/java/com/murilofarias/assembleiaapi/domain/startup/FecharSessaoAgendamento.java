package com.murilofarias.assembleiaapi.domain.startup;


import com.murilofarias.assembleiaapi.domain.model.Pauta;
import com.murilofarias.assembleiaapi.domain.model.SessaoStatus;
import com.murilofarias.assembleiaapi.domain.usecase.FecharSessaoUseCase;
import com.murilofarias.assembleiaapi.infra.PautaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
@Order(1)
class FecharSessaoAgendamento
        implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(FecharSessaoAgendamento.class);

    @Autowired
    PautaRepository pautaRepository;

    @Autowired
    FecharSessaoUseCase fecharSessaoUseCase;

    @Autowired
    ThreadPoolTaskScheduler taskScheduler;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<Pauta> pautas = pautaRepository.findAllByStatus(SessaoStatus.ABERTA);
        logger.info("Quantidade de pautas abertas " + pautas.size());
        pautas.forEach(pauta ->
                {
                    OffsetDateTime dataFechamento = pauta.getDataAbertura().plus(pauta.getDuracaoSessao(), ChronoUnit.MINUTES);
                    if (dataFechamento.isBefore(OffsetDateTime.now())) {
                        taskScheduler.submit(fecharSessaoUseCase.execute(pauta.getId()));
                        logger.info("Pauta com id" + pauta.getId() + " foi submetida para ter sessão fechada"  );
                    } else {
                        taskScheduler.schedule(fecharSessaoUseCase.execute(pauta.getId()), Date.from(dataFechamento.toInstant()));
                        logger.info("Pauta com id" + pauta.getId() + " teve fechamento agendado para a data " + Date.from(dataFechamento.toInstant()));
                    }
                }
        );

    }

}
