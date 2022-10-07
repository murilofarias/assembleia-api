package com.murilofarias.assembleiaapi.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.murilofarias.assembleiaapi.domain.model.Associado;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssociadoResponseDto {
    Long id;

    String cpf;

    String nome;


    public AssociadoResponseDto(Associado associado){
        this.id = associado.getId();
        this.cpf = associado.getCpf();
        this.nome = associado.getNome();
    }
}
