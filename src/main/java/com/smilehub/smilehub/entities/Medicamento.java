package com.smilehub.smilehub.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "medicamento")
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_prescricao", nullable = false)
    private Prescricao prescricao;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String dosagem;

    @Column(nullable = false)
    private String frequencia;

    @Column(nullable = false)
    private String duracao;

    protected Medicamento() {
    }

    public Medicamento(Prescricao prescricao, String nome, String dosagem, String frequencia, String duracao) {
        this.prescricao = prescricao;
        this.nome = nome;
        this.dosagem = dosagem;
        this.frequencia = frequencia;
        this.duracao = duracao;
    }

    public Long getId() {
        return id;
    }

    public Prescricao getPrescricao() {
        return prescricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDosagem() {
        return dosagem;
    }

    public void setDosagem(String dosagem) {
        this.dosagem = dosagem;
    }

    public String getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(String frequencia) {
        this.frequencia = frequencia;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }
}
