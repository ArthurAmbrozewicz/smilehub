package com.smilehub.smilehub.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "dente",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_odontograma", "numero"})
)
public class Dente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_odontograma", nullable = false)
    private Odontograma odontograma;

    @Column(nullable = false)
    private Integer numero;

    @Column(nullable = false)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    protected Dente() {
    }

    public Dente(Odontograma odontograma, Integer numero, String status, String observacoes) {
        this.odontograma = odontograma;
        this.numero = numero;
        this.status = status;
        this.observacoes = observacoes;
    }

    public Long getId() {
        return id;
    }

    public Odontograma getOdontograma() {
        return odontograma;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
