package com.murilofarias.assembleiaapi.infra;

import com.murilofarias.assembleiaapi.domain.model.Pauta;
import com.murilofarias.assembleiaapi.domain.model.ResultadoSessao;
import com.murilofarias.assembleiaapi.domain.model.SessaoStatus;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;


public interface ResultadoSessaoRepository extends PagingAndSortingRepository<ResultadoSessao, Long> {
    List<ResultadoSessao> findAll();

    Optional<ResultadoSessao> findByPautaId(Long pautaId);
}
