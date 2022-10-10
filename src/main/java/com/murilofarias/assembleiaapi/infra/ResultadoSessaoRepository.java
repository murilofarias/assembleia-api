package com.murilofarias.assembleiaapi.infra;

import com.murilofarias.assembleiaapi.domain.model.ResultadoSessao;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface ResultadoSessaoRepository extends PagingAndSortingRepository<ResultadoSessao, Long> {
}
