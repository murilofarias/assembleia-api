package com.murilofarias.assembleiaapi.infra;

import com.murilofarias.assembleiaapi.domain.model.Pauta;
import com.murilofarias.assembleiaapi.domain.model.SessaoStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;


public interface PautaRepository extends PagingAndSortingRepository<Pauta, Long> {

    @Query("select e from Pauta e left join fetch e.votos where e.id = ?1")
    Optional<Pauta> findByIdEager(long id);

    List<Pauta> findAllByStatus(SessaoStatus status);
}