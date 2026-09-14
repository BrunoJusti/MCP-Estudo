package com.estudo.mcpobras.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cnpj;

    private String razaoSocial;

    private String segmento;

    public Empresa() {
        // construtor exigido pelo JPA
    }

    public Empresa(String cnpj, String razaoSocial, String segmento) {
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.segmento = segmento;
    }

    public Long getId() {
        return id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getSegmento() {
        return segmento;
    }
}
