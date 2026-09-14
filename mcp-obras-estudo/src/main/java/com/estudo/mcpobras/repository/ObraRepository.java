package com.estudo.mcpobras.repository;

import com.estudo.mcpobras.domain.Obra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObraRepository extends JpaRepository<Obra, Long> {

    List<Obra> findByEmpresaId(Long empresaId);
}
