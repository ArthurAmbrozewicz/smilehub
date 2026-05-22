package com.smilehub.smilehub.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "pacientes")
@DiscriminatorValue("PACIENTE")
public class Paciente extends Usuario {

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String telefone;

    protected Paciente() {
    }

    public Paciente(String nome, String email, String senha, Ativo ativo, String cpf, String telefone) {
        super(nome, email, senha, ativo);
        this.cpf = cpf;
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
