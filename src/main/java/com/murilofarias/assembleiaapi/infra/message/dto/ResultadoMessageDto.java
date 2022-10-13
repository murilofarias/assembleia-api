package com.murilofarias.assembleiaapi.infra.message.dto;

import com.murilofarias.assembleiaapi.domain.model.Pauta;
import com.murilofarias.assembleiaapi.domain.model.ResultadoSessao;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResultadoMessageDto {
    long id;

    long sim;

    long nao;

    PautaMessageDto pauta;

    public ResultadoMessageDto(){}
    public ResultadoMessageDto(ResultadoSessao resultadoSessao){
        this.id = resultadoSessao.getId();
        this.sim = resultadoSessao.getSim();
        this.nao = resultadoSessao.getNao();
        this.pauta = new PautaMessageDto(resultadoSessao.getPauta());
    }
}
