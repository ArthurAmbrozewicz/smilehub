package com.smilehub.smilehub.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "odontograma")
public class Odontograma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_paciente", nullable = false, unique = true)
    private Paciente paciente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_dentista", nullable = false)
    private Dentista dentista;

    @Column(name = "data_ultima_alteracao", nullable = false)
    private LocalDateTime dataUltimaAlteracao;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    protected Odontograma() {
    }

    public Odontograma(Paciente paciente, Dentista dentista, String observacoes) {
        this.paciente = paciente;
        this.dentista = dentista;
        this.observacoes = observacoes;
    }

    @PrePersist
    void prePersist() {
        if (this.dataUltimaAlteracao == null) {
            this.dataUltimaAlteracao = LocalDateTime.now();
        }
    }

    @PreUpdate
    void preUpdate() {
        this.dataUltimaAlteracao = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Dentista getDentista() {
        return dentista;
    }

    public void setDentista(Dentista dentista) {
        this.dentista = dentista;
    }

    public LocalDateTime getDataUltimaAlteracao() {
        return dataUltimaAlteracao;
    }

    public void setDataUltimaAlteracao(LocalDateTime dataUltimaAlteracao) {
        this.dataUltimaAlteracao = dataUltimaAlteracao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
