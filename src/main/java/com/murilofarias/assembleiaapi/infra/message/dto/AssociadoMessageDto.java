package com.murilofarias.assembleiaapi.infra.message.dto;

import com.murilofarias.assembleiaapi.domain.model.Associado;
import lombok.Getter;
import lombok.Setter;



@Setter
@Getter
public class AssociadoMessageDto {
    private long id;

    private String nome;

    private String cpf;

    public AssociadoMessageDto(){}
    public AssociadoMessageDto(Associado associado){
        this.id = associado.getId();
        this.nome = associado.getNome();
        this.cpf = associado.getCpf();
    }

}