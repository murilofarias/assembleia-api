package com.murilofarias.assembleiaapi.domain.model;

import com.murilofarias.assembleiaapi.domain.error.DomainException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
@Entity
public class Pauta {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String titulo;

    @Column
    private String descricao;

    @OneToMany(mappedBy = "pauta", cascade = CascadeType.ALL)
    private List<Voto> votos =  new ArrayList<>();

    @Column
    private SessaoStatus status;

    @Column
    private Integer duracaoSessao;

    @Column
    private OffsetDateTime dataAbertura;

    public Pauta(String titulo, String descricao){
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = SessaoStatus.NAO_INICIADA;
    }

    public void abrirSessao(Integer duracaoSessao) throws DomainException {
        if(!status.equals(SessaoStatus.NAO_INICIADA))
            throw new DomainException("Erro na abertura da Sessão", "A sessão já foi aberta!");

        if(duracaoSessao > 0 && duracaoSessao <= 30) {
            this.duracaoSessao = duracaoSessao;
            this.status = SessaoStatus.ABERTA;
            this.dataAbertura = OffsetDateTime.now();
        }
        else
            throw new DomainException("Erro na abertura da Sessão", "Duração da sessão deve ser maior que 0 e menor ou igual a 30 minutos");



    }

    public void fecharSessao(){
        if(!status.equals(SessaoStatus.ABERTA))
            throw new DomainException("Erro na abertura da Sessão", "A sessão precisa estar aberta!");

        this.status = SessaoStatus.EM_APURACAO;
    }

    public void finalizarApuracao(){
        if(!status.equals(SessaoStatus.EM_APURACAO))
            throw new DomainException("Erro na abertura da Sessão", "A sessão precisa estar em apuração!");

        this.status = SessaoStatus.REALIZADA;
    }
}
