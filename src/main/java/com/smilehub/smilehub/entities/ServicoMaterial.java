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
        name = "servico_material",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_servico_material",
                columnNames = {"id_servico", "id_material"}
        )
)
public class ServicoMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_servico", nullable = false)
    private Servico servico;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_material", nullable = false)
    private Material material;

    @Column(nullable = false)
    private int quantidade;

    protected ServicoMaterial() {
    }

    public ServicoMaterial(Servico servico, Material material, int quantidade) {
        this.servico = servico;
        this.material = material;
        this.quantidade = quantidade;
    }

    public Long getId() {
        return id;
    }

    public Servico getServico() {
        return servico;
    }

    public Material getMaterial() {
        return material;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
