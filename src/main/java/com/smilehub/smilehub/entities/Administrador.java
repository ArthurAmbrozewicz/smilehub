package com.smilehub.smilehub.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "administradores")
@DiscriminatorValue("ADMINISTRADOR")
public class Administrador extends Usuario {

    protected Administrador() {
    }

    public Administrador(String nome, String email, String senha, Ativo ativo) {
        super(nome, email, senha, ativo);
    }
}
