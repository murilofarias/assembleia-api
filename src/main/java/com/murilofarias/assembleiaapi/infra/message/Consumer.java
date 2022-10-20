package com.murilofarias.assembleiaapi.infra.message;

import com.murilofarias.assembleiaapi.domain.model.ResultadoSessao;
import com.murilofarias.assembleiaapi.domain.model.Voto;
import com.murilofarias.assembleiaapi.domain.usecase.ValidarVotoUseCase;
import com.murilofarias.assembleiaapi.infra.message.dto.ResultadoMessageDto;
import com.murilofarias.assembleiaapi.infra.message.dto.VotoMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Consumer {

    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    ValidarVotoUseCase validarVotoUseCase;



    @KafkaListener(topics = "votos", groupId = "group_id")
    public void consumeVoto(VotoMessageDto voto) throws IOException {
        logger.info(String.format("#### -> Voto com cpf %s, na pauta de id %s, foi recebido para validação" ,   voto.getAssociado().getCpf(), voto.getValor().toString()), voto.getPauta().getId());
        Voto votoValidado = validarVotoUseCase.execute(voto.getId(), voto.getAssociado().getCpf());
        logger.info(String.format("#### ->Associado com  cpf %s, na pauta de id %s, votou em %s e voto é  %s",  voto.getAssociado().getCpf(), voto.getPauta().getId(),   voto.getValor().toString(), votoValidado.getStatus().toString()));
    }
}
