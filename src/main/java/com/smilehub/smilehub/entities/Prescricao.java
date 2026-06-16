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
import java.time.LocalDateTime;

@Entity
@Table(name = "prescricao")
public class Prescricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_consulta", nullable = false, unique = true)
    private Consulta consulta;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "data_emissao", updatable = false)
    private LocalDateTime dataEmissao;

    protected Prescricao() {
    }

    public Prescricao(Consulta consulta, String observacoes) {
        this.consulta = consulta;
        this.observacoes = observacoes;
    }

    @PrePersist
    void prePersist() {
        if (this.dataEmissao == null) {
            this.dataEmissao = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }
}
