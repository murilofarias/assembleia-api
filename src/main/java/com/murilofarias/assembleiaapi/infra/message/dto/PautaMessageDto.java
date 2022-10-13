package com.murilofarias.assembleiaapi.infra.message.dto;

import com.murilofarias.assembleiaapi.controller.dto.response.VotoSemPautaDto;
import com.murilofarias.assembleiaapi.domain.model.Pauta;
import com.murilofarias.assembleiaapi.domain.model.SessaoStatus;
import com.murilofarias.assembleiaapi.domain.model.Voto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class PautaMessageDto {

    private long id;

    private String titulo;

    private String descricao;

    private SessaoStatus status;

    private Integer duracaoSessao;

    private OffsetDateTime dataAbertura;

    public PautaMessageDto(){}
    public PautaMessageDto(Pauta pauta){
        this.id = pauta.getId();
        this.titulo = pauta.getTitulo();
        this.descricao = pauta.getDescricao();
        this.status = pauta.getStatus();
        this.duracaoSessao = pauta.getDuracaoSessao();
    }
}
