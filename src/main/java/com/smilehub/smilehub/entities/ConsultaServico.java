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
import java.math.BigDecimal;

@Entity
@Table(
        name = "consulta_servico",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_consulta_servico",
                columnNames = {"id_consulta", "id_servico"}
        )
)
public class ConsultaServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_consulta", nullable = false)
    private Consulta consulta;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_servico", nullable = false)
    private Servico servico;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    protected ConsultaServico() {
    }

    public ConsultaServico(Consulta consulta, Servico servico, BigDecimal valor) {
        this.consulta = consulta;
        this.servico = servico;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public Servico getServico() {
        return servico;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
