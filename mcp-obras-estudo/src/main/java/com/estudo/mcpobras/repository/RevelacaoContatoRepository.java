package com.estudo.mcpobras.repository;

import com.estudo.mcpobras.domain.RevelacaoContato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RevelacaoContatoRepository extends JpaRepository<RevelacaoContato, Long> {

    Optional<RevelacaoContato> findByEmpresaIdAndContatoId(Long empresaId, Long contatoId);
}