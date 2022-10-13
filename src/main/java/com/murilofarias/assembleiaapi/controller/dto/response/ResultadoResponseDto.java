package com.murilofarias.assembleiaapi.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.murilofarias.assembleiaapi.domain.model.Associado;
import com.murilofarias.assembleiaapi.domain.model.ResultadoSessao;
import lombok.Getter;




@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultadoResponseDto {
    long id;

    long sim;

    long nao;

    PautaResponseDto pauta;



    public ResultadoResponseDto(ResultadoSessao resultadoSessao){
        this.id = resultadoSessao.getId();
        this.sim = resultadoSessao.getSim();
        this.nao = resultadoSessao.getNao();
        this.pauta = new PautaResponseDto(resultadoSessao.getPauta());
    }
}