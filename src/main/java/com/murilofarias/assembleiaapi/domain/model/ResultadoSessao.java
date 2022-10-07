package com.murilofarias.assembleiaapi.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ResultadoSessao{

    @Id
    @GeneratedValue
    private long id;

    @Column
    private long sim;

    @Column
    private long nao;

    @OneToOne
    private Pauta pauta;

    public ResultadoSessao(Pauta pauta){
        this.pauta = pauta;
        pauta.getVotos().forEach(voto -> {
            if(!voto.getStatus().equals(VotoStatus.VALIDO))
                return;

            if(voto.getValor().equals(VotoValor.SIM))
                this.sim += 1;
            else
                this.nao += 1;
        });
    }
}
