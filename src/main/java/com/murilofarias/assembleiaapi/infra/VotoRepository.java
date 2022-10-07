package com.murilofarias.assembleiaapi.infra;

import com.murilofarias.assembleiaapi.domain.model.Voto;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VotoRepository extends PagingAndSortingRepository<Voto, Long> {}
