package com.murilofarias.assembleiaapi.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.murilofarias.assembleiaapi.domain.model.Pauta;
import com.murilofarias.assembleiaapi.domain.model.SessaoStatus;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PautaComVotosDto{

    private long id;

    private String titulo;

    private String descricao;

    private SessaoStatus status;

    private List<VotoSemPautaDto> votos;

    private Integer duracaoSessao;

    public PautaComVotosDto(Pauta pauta){
        this.id = pauta.getId();
        this.titulo = pauta.getTitulo();
        this.descricao = pauta.getDescricao();
        this.status = pauta.getStatus();
        this.duracaoSessao = pauta.getDuracaoSessao();
        this.votos = pauta.getVotos().stream().map(VotoSemPautaDto::new).collect(Collectors.toList());
    }
}