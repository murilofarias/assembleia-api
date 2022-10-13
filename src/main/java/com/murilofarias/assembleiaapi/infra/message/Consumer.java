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

    private final Logger logger = LoggerFactory.getLogger(Producer.class);

    @Autowired
    ValidarVotoUseCase validarVotoUseCase;


    @KafkaListener(topics = "resultadoSessao", groupId = "group_id")
    public void consumeResultado(ResultadoMessageDto resultadoSessao) throws IOException  {
        logger.info(String.format("#### -> Consumed message -> %s", resultadoSessao.getPauta().getTitulo()));
    }

    @KafkaListener(topics = "votos", groupId = "group_id")
    public void consumeVoto(VotoMessageDto voto) throws IOException {
        logger.info(String.format("#### -> validação do voto do cpf %s em  -> %s" ,  voto.getValor().toString()));
        Voto votoValidado = validarVotoUseCase.execute(voto.getId(), voto.getAssociado().getCpf());
        logger.info(String.format("#### -> %s votou em  -> %s e voto é  %s",  voto.getAssociado().getCpf(),  voto.getValor().toString(), votoValidado.getStatus().toString()));
    }
}
