package com.murilofarias.assembleiaapi.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CadastrarAssociadoRequestDto {

    @NotBlank(message = "cpf é obrigatório")
    @CPF(message = "cpf não é válido")
    String cpf;

    String nome;
}
