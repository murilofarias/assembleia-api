package com.murilofarias.assembleiaapi.infra;

import com.murilofarias.assembleiaapi.domain.model.Voto;
import com.murilofarias.assembleiaapi.domain.model.VotoStatus;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface VotoRepository extends PagingAndSortingRepository<Voto, Long> {
    List<Voto> findAllByStatus(VotoStatus status);
}
