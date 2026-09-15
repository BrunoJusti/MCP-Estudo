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

    private Integer creditosEmailRadar;

    private String apiKey;

    private String loginUsername;

    private String loginPassword;

    public Empresa() {
    }

    public Empresa(String cnpj, String razaoSocial, String segmento, Integer creditosEmailRadar,
                   String apiKey, String loginUsername, String loginPassword) {
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.segmento = segmento;
        this.creditosEmailRadar = creditosEmailRadar;
        this.apiKey = apiKey;
        this.loginUsername = loginUsername;
        this.loginPassword = loginPassword;
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

    public Integer getCreditosEmailRadar() {
        return creditosEmailRadar;
    }

    public void setCreditosEmailRadar(Integer creditosEmailRadar) {
        this.creditosEmailRadar = creditosEmailRadar;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public String getLoginPassword() {
        return loginPassword;
    }
}