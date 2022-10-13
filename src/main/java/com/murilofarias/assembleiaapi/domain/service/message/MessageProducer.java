package com.murilofarias.assembleiaapi.domain.service.message;

import com.murilofarias.assembleiaapi.domain.model.ResultadoSessao;
import com.murilofarias.assembleiaapi.domain.model.Voto;

public interface MessageProducer {
    public void sendResultadoSessao(ResultadoSessao resultadoSessao);
    public void sendVoto(Voto voto);
}
