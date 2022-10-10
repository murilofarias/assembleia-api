package com.murilofarias.assembleiaapi.controller;

import com.murilofarias.assembleiaapi.controller.dto.request.AbrirSessaoRequestDto;
import com.murilofarias.assembleiaapi.controller.dto.request.CadastrarPautaRequestDto;
import com.murilofarias.assembleiaapi.controller.dto.request.CadastrarVotoRequestDto;
import com.murilofarias.assembleiaapi.controller.dto.response.PautaResponseDto;
import com.murilofarias.assembleiaapi.controller.dto.response.VotoResponseDto;
import com.murilofarias.assembleiaapi.domain.model.Pauta;
import com.murilofarias.assembleiaapi.domain.model.Voto;
import com.murilofarias.assembleiaapi.domain.usecase.AbrirSessaoUseCase;
import com.murilofarias.assembleiaapi.domain.usecase.CadastrarPautaUseCase;
import com.murilofarias.assembleiaapi.domain.usecase.CadastrarVotoUseCase;
import com.murilofarias.assembleiaapi.domain.usecase.ListarPautasUseCase;
import com.murilofarias.assembleiaapi.util.CpfParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
public class VotacaoController {

    @Autowired
    CadastrarVotoUseCase cadastrarVotoUseCase;

    @Autowired
    CadastrarPautaUseCase cadastrarPautaUseCase;

    @Autowired
    ListarPautasUseCase listarPautasUseCase;

    @Autowired
    AbrirSessaoUseCase abrirSessaoUseCase;

    @PostMapping("/pautas")
    public ResponseEntity<PautaResponseDto> cadastrarPauta(@Valid @RequestBody CadastrarPautaRequestDto cadastrarPautaRequestDto) {

        Pauta novaPauta = cadastrarPautaUseCase.execute(cadastrarPautaRequestDto.getTitulo(), cadastrarPautaRequestDto.getDescricao());
        return new ResponseEntity<>(new PautaResponseDto(novaPauta), HttpStatus.CREATED);
    }


    @GetMapping("/pautas")
    public ResponseEntity<Page<PautaResponseDto>> listarPautas(
            @Min(0) @RequestParam(value = "page", defaultValue = "0") Integer page,
            @Min(0) @RequestParam(value = "size", defaultValue = "5") Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Pauta> pautas = listarPautasUseCase.execute(pageable);
        Page<PautaResponseDto> pautasResponseDto = pautas.map(PautaResponseDto::new);
        return new ResponseEntity<>(pautasResponseDto, HttpStatus.CREATED);
    }

    @PatchMapping("/abrir-sessao/pautas/{id}")
    public ResponseEntity<PautaResponseDto> abrirSessao(
            @PathVariable("id") Long id,
            @Valid @RequestBody AbrirSessaoRequestDto abrirSessaoRequestDto) {

        Pauta novaPauta = abrirSessaoUseCase.execute(id, abrirSessaoRequestDto.getDuracao());
        return new ResponseEntity<>(new PautaResponseDto(novaPauta), HttpStatus.CREATED);
    }


    @PostMapping("/votos")
    public ResponseEntity<VotoResponseDto> cadastrarVoto(@Valid @RequestBody CadastrarVotoRequestDto cadastrarVotoRequestDto) {
        String cpfFormatado = CpfParser.eliminateDotsAndDashes(cadastrarVotoRequestDto.getCpfAssociado());
        Voto novoVoto = cadastrarVotoUseCase.execute(cadastrarVotoRequestDto.getPautaId(), cpfFormatado, cadastrarVotoRequestDto.getVoto());
        return new ResponseEntity<>(new VotoResponseDto(novoVoto), HttpStatus.CREATED);
    }
}