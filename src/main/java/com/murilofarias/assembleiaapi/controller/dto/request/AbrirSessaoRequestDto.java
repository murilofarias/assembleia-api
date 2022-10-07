package com.murilofarias.assembleiaapi.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AbrirSessaoRequestDto {

    @Min(1)
    @Max(30)
    Integer duracao;

}
