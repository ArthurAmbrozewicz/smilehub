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
@Table(name = "material")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private int quantidade;

    @Column(name = "quantidade_inicial", nullable = false)
    private int quantidadeInicial;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "alerta_estoque_baixo", nullable = false)
    private boolean alertaEstoqueBaixo = false;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    protected Material() {
    }

    public Material(String nome, int quantidade, Usuario usuario) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.quantidadeInicial = quantidade;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getQuantidadeInicial() {
        return quantidadeInicial;
    }

    public void setQuantidadeInicial(int quantidadeInicial) {
        this.quantidadeInicial = quantidadeInicial;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isAlertaEstoqueBaixo() {
        return alertaEstoqueBaixo;
    }

    public void setAlertaEstoqueBaixo(boolean alertaEstoqueBaixo) {
        this.alertaEstoqueBaixo = alertaEstoqueBaixo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
