package com.murilofarias.assembleiaapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Voto{

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Associado associado;

    @ManyToOne
    private Pauta pauta;

    @Column
    private VotoStatus status;
    @Column
    private OffsetDateTime data;
    @Column
    private VotoValor valor;

    public Voto(Associado associado, Pauta pauta, VotoValor valor){
        this.associado = associado;
        this.pauta = pauta;
        this.valor = valor;
        this.status = VotoStatus.PENDENTE;
        this.data = OffsetDateTime.now();
    }

    public void validarVoto(){
        status = VotoStatus.VALIDO;
    }

    public void invalidarVoto(){
        status = VotoStatus.INVALIDO;
    }

}
