package com.murilofarias.assembleiaapi.domain.model;

public enum VotoValor {
    SIM("Sim", "S"),
    NAO("Não", "N");

    private String nome;

    private String simbolo;
    VotoValor(String nome, String simbolo){
        this.nome = nome;
        this.simbolo = simbolo;
    }
}
