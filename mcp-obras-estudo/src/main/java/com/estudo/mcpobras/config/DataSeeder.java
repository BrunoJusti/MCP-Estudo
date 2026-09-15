package com.estudo.mcpobras.config;

import com.estudo.mcpobras.domain.Empresa;
import com.estudo.mcpobras.domain.Obra;
import com.estudo.mcpobras.repository.EmpresaRepository;
import com.estudo.mcpobras.repository.ObraRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final EmpresaRepository empresaRepository;
    private final ObraRepository obraRepository;

    public DataSeeder(EmpresaRepository empresaRepository, ObraRepository obraRepository) {
        this.empresaRepository = empresaRepository;
        this.obraRepository = obraRepository;
    }

    @Override
    public void run(String... args) {
        if (empresaRepository.count() > 0) {
            return;
        }

        Empresa construtoraAlfa = empresaRepository.save(
                new Empresa("11.111.111/0001-11", "Construtora Alfa Ltda", "Residencial", 5,
                        "cc-alfa-9f8a2b", "alfa", "{noop}123456"));
        Empresa construtoraBeta = empresaRepository.save(
                new Empresa("22.222.222/0001-22", "Construtora Beta S.A.", "Comercial", 2,
                        "cc-beta-7d3c1e", "beta", "{noop}123456"));
        Empresa construtoraGama = empresaRepository.save(
                new Empresa("33.333.333/0001-33", "Construtora Gama Engenharia", "Industrial", 0,
                        "cc-gama-4e2a90", "gama", "{noop}123456"));

        obraRepository.save(new Obra("Residencial Jardim das Flores", "Ribeirão Preto", "Residencial", construtoraAlfa));
        obraRepository.save(new Obra("Condomínio Vila Verde", "Sertãozinho", "Residencial", construtoraAlfa));
        obraRepository.save(new Obra("Shopping Center Norte", "Ribeirão Preto", "Comercial", construtoraBeta));
        obraRepository.save(new Obra("Torre Empresarial Beta", "Franca", "Comercial", construtoraBeta));
        obraRepository.save(new Obra("Galpão Logístico Gama", "Sertãozinho", "Industrial", construtoraGama));
        obraRepository.save(new Obra("Planta Industrial Norte", "Ribeirão Preto", "Industrial", construtoraGama));
    }
}