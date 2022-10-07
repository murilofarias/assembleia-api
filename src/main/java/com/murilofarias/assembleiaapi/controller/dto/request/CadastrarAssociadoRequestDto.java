package com.murilofarias.assembleiaapi.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CadastrarAssociadoRequestDto {

    @NotBlank(message = "cpf é obrigatório")
    String cpf;

    String nome;
}
