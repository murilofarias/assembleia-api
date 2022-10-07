package com.murilofarias.assembleiaapi.infra;

import com.murilofarias.assembleiaapi.domain.model.Associado;
import com.murilofarias.assembleiaapi.domain.model.Pauta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.Optional;


public interface PautaRepository extends PagingAndSortingRepository<Pauta, Long> {

    @Query("select e from Pauta e left join fetch e.votos where e.id = ?1")
    Optional<Pauta> findByIdEager(long id);
}