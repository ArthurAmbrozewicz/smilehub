package com.smilehub.smilehub.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "dentista_especialidade",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_dentista_especialidade",
                columnNames = {"id_dentista", "id_especialidade"}
        )
)
public class DentistaEspecialidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_dentista", nullable = false)
    private Dentista dentista;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_especialidade", nullable = false)
    private Especialidade especialidade;

    @Column(name = "data_cadastro", updatable = false)
    private LocalDateTime dataCadastro;

    protected DentistaEspecialidade() {
    }

    public DentistaEspecialidade(Dentista dentista, Especialidade especialidade) {
        this.dentista = dentista;
        this.especialidade = especialidade;
    }

    @PrePersist
    void prePersist() {
        this.dataCadastro = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Dentista getDentista() {
        return dentista;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
}
