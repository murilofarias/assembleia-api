package com.murilofarias.assembleiaapi.infra.message.dto;

import com.murilofarias.assembleiaapi.domain.model.Voto;
import com.murilofarias.assembleiaapi.domain.model.VotoStatus;
import com.murilofarias.assembleiaapi.domain.model.VotoValor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Setter
@Getter
public class VotoMessageDto {
    private long id;


    private PautaMessageDto pauta;

    private AssociadoMessageDto associado;

    private VotoStatus status;
    private OffsetDateTime data;
    private VotoValor valor;

    public VotoMessageDto(){}
    public VotoMessageDto(Voto voto){
        this.id = voto.getId();
        this.pauta = new PautaMessageDto(voto.getPauta());
        this.associado = new AssociadoMessageDto(voto.getAssociado());
        this.status = voto.getStatus();
        this.data = voto.getData();
        this.valor = voto.getValor();
    }

}
