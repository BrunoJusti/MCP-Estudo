package com.estudo.mcpobras.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class RevelacaoContato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Empresa empresa;

    @ManyToOne
    private Contato contato;

    public RevelacaoContato() {
    }

    public RevelacaoContato(Empresa empresa, Contato contato) {
        this.empresa = empresa;
        this.contato = contato;
    }

    public Long getId() {
        return id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public Contato getContato() {
        return contato;
    }
}