package com.estudo.mcpobras.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String cargo;

    private String email;

    private String dominio;

    public Contato() {
    }

    public Contato(String nome, String cargo, String email, String dominio) {
        this.nome = nome;
        this.cargo = cargo;
        this.email = email;
        this.dominio = dominio;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCargo() {
        return cargo;
    }

    public String getEmail() {
        return email;
    }

    public String getDominio() {
        return dominio;
    }
}