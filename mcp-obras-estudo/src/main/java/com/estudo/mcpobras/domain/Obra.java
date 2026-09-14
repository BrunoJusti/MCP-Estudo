package com.estudo.mcpobras.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Obra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String cidade;

    private String segmento;

    @ManyToOne
    private Empresa empresa;

    public Obra() {
        // construtor exigido pelo JPA
    }

    public Obra(String nome, String cidade, String segmento, Empresa empresa) {
        this.nome = nome;
        this.cidade = cidade;
        this.segmento = segmento;
        this.empresa = empresa;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCidade() {
        return cidade;
    }

    public String getSegmento() {
        return segmento;
    }

    public Empresa getEmpresa() {
        return empresa;
    }
}
