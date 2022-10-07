package com.murilofarias.assembleiaapi.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.murilofarias.assembleiaapi.domain.model.Associado;
import com.murilofarias.assembleiaapi.domain.model.Voto;
import com.murilofarias.assembleiaapi.domain.model.VotoStatus;
import com.murilofarias.assembleiaapi.domain.model.VotoValor;
import lombok.Getter;


@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VotoResponseDto {
    Long id;

    AssociadoResponseDto associado;

    PautaResponseDto pauta;

    VotoStatus status;

    VotoValor valor;



    public VotoResponseDto(Voto voto){
        this.id = voto.getId();
        this.associado = new AssociadoResponseDto(voto.getAssociado());
        this.pauta = new PautaResponseDto(voto.getPauta());
        this.status = voto.getStatus();
        this.valor = voto.getValor();
    }
}