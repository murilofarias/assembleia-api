package com.murilofarias.assembleiaapi.infra;

import com.murilofarias.assembleiaapi.domain.model.Associado;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface AssociadoRepository extends PagingAndSortingRepository<Associado, Long> {
    boolean existsByCpf(String cpf);

    Optional<Associado> findByCpf(String cpf);
}
