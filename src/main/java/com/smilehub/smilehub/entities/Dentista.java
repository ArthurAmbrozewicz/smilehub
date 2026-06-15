package com.smilehub.smilehub.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "dentistas")
@DiscriminatorValue("DENTISTA")
public class Dentista extends Usuario {

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String cro;

    protected Dentista() {
    }

    public Dentista(String nome, String email, String senha, boolean ativo, String cpf, String cro) {
        super(nome, email, senha, ativo);
        this.cpf = cpf;
        this.cro = cro;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCro() {
        return cro;
    }

    public void setCro(String cro) {
        this.cro = cro;
    }
}
