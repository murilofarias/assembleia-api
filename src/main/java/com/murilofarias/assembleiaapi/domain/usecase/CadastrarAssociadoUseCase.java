package com.murilofarias.assembleiaapi.domain.usecase;

import com.murilofarias.assembleiaapi.domain.error.DomainException;
import com.murilofarias.assembleiaapi.domain.model.Associado;
import com.murilofarias.assembleiaapi.infra.AssociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CadastrarAssociadoUseCase {

    @Autowired
    AssociadoRepository associadoRepository;

    public Associado execute(String cpf, String nome){
        Associado novoAssociado;
        if(!associadoRepository.existsByCpf(cpf))
            novoAssociado = associadoRepository.save(new Associado(cpf, nome));
        else{
            throw new DomainException("Erro na Verificação de unicidade de cpf", "Já existe Associado com o cpf dado");
        }
        return novoAssociado;
    }
}
