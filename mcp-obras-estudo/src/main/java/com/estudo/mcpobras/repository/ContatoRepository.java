package com.estudo.mcpobras.repository;

import com.estudo.mcpobras.domain.Contato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContatoRepository extends JpaRepository<Contato, Long> {

    List<Contato> findByDominioIgnoreCase(String dominio);
}