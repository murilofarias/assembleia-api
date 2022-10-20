package com.murilofarias.assembleiaapi.infra.message;

import com.murilofarias.assembleiaapi.domain.model.ResultadoSessao;
import com.murilofarias.assembleiaapi.domain.model.Voto;
import com.murilofarias.assembleiaapi.domain.service.message.MessageProducer;
import com.murilofarias.assembleiaapi.infra.message.dto.ResultadoMessageDto;
import com.murilofarias.assembleiaapi.infra.message.dto.VotoMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer implements MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "resultadoSessao";
    private static final String TOPIC2 = "votos";
    @Autowired
    private KafkaTemplate<String, ResultadoMessageDto> kafkaTemplateResultados;

    @Autowired
    private KafkaTemplate<String, VotoMessageDto> kafkaTemplateVotos;


    @Override
    public void sendResultadoSessao(ResultadoSessao resultadoSessao) {
        logger.info(String.format("#### -> Producing message -> %s", resultadoSessao.getPauta().getTitulo()));

        this.kafkaTemplateResultados.send(TOPIC, new ResultadoMessageDto(resultadoSessao));
    }

    @Override
    public void sendVoto(Voto voto) {
        logger.info(String.format("#### -> Voto com cpf %s com valor %s foi enviado para validação" ,   voto.getAssociado().getCpf(), voto.getValor().toString()));
        this.kafkaTemplateVotos.send(TOPIC2, new VotoMessageDto(voto));
    }
}
