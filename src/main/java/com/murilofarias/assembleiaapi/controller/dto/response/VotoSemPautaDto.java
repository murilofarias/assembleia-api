package com.murilofarias.assembleiaapi.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.murilofarias.assembleiaapi.domain.model.Voto;
import com.murilofarias.assembleiaapi.domain.model.VotoStatus;
import com.murilofarias.assembleiaapi.domain.model.VotoValor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VotoSemPautaDto {
    Long id;

    AssociadoResponseDto associado;

    VotoStatus status;

    OffsetDateTime data;

    VotoValor valor;



    public VotoSemPautaDto(Voto voto){
        this.id = voto.getId();
        this.associado = new AssociadoResponseDto(voto.getAssociado());
        this.data = voto.getData();
        this.status = voto.getStatus();
        this.valor = voto.getValor();
    }
}
