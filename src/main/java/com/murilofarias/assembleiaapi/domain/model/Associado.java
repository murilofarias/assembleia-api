package com.murilofarias.assembleiaapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Associado {

    @Id
    @GeneratedValue
    private long id;

    @Column(unique=true)
    private String cpf;

    @Column
    private String nome;

    public Associado(String cpf, String nome){
        this.cpf = cpf;
        this.nome = nome;
    }

    public Voto votarPauta(Pauta pauta, VotoValor valor){
        return new Voto(this, pauta, valor);
    }


}
