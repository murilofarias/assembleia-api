package com.murilofarias.assembleiaapi.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CadastrarVotoRequestDto {

    @NotNull(message="pautaId é obrigatória")
    Long pautaId;

    @CPF(message = "cpf não é válido")
    @NotBlank(message = "cpf não poder ser branco")
    String cpfAssociado;

    @Pattern(regexp = "^[SN]$", message="voto precisa ser 'S' ou 'N'")
    @NotNull(message="Voto é obrigatório")
    String voto;
}