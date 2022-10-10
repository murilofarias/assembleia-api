package com.murilofarias.assembleiaapi.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.murilofarias.assembleiaapi.domain.model.Pauta;
import com.murilofarias.assembleiaapi.domain.model.SessaoStatus;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PautaResponseDto{

    private long id;

    private String titulo;

    private String descricao;

    private SessaoStatus status;

    private Integer duracaoSessao;

    public PautaResponseDto(Pauta pauta){
        this.id = pauta.getId();
        this.titulo = pauta.getTitulo();
        this.descricao = pauta.getDescricao();
        this.status = pauta.getStatus();
        this.duracaoSessao = pauta.getDuracaoSessao();
    }
}
