package com.murilofarias.assembleiaapi.controller;

import com.murilofarias.assembleiaapi.controller.dto.request.CadastrarAssociadoRequestDto;
import com.murilofarias.assembleiaapi.controller.dto.response.AssociadoResponseDto;
import com.murilofarias.assembleiaapi.domain.model.Associado;
import com.murilofarias.assembleiaapi.domain.usecase.CadastrarAssociadoUseCase;
import com.murilofarias.assembleiaapi.domain.usecase.ListarAssociadosUseCase;
import com.murilofarias.assembleiaapi.util.CpfParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
public class GestaoAssociadoController {

    @Autowired
    CadastrarAssociadoUseCase cadastrarAssociadoUseCase;

    @Autowired
    ListarAssociadosUseCase listarAssociadosUseCase;

    @PostMapping("/associados")
    public ResponseEntity<AssociadoResponseDto> cadastrarAssociado(
            @Valid @RequestBody CadastrarAssociadoRequestDto cadastrarAssociadoReqDto) {

        String cpfFormatado = CpfParser.eliminateDotsAndDashes(cadastrarAssociadoReqDto.getCpf());
        Associado novoAssociado = cadastrarAssociadoUseCase.execute(cpfFormatado, cadastrarAssociadoReqDto.getNome());
        AssociadoResponseDto  associadoResponseDto = new AssociadoResponseDto(novoAssociado);
        return new ResponseEntity<>(associadoResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/associados")
    public ResponseEntity<Page<AssociadoResponseDto>> listarAssociados(
            @Min(0) @RequestParam(value = "page", defaultValue = "0") Integer page,
            @Min(0) @RequestParam(value = "size", defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nome"));
        Page<Associado> associados = listarAssociadosUseCase.execute(pageable);
        Page<AssociadoResponseDto> associadosResponseDto = associados.map(AssociadoResponseDto::new);
        return new ResponseEntity<>(associadosResponseDto, HttpStatus.OK);
    }
}
