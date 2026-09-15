package com.estudo.mcpobras.externo;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SnovIoSimulado {

    public List<ContatoEncontrado> buscarContatosPorDominio(String dominio) {
        String dominioLimpo = dominio.trim().toLowerCase();

        return List.of(
                new ContatoEncontrado("Ana Ferreira", "Diretora Comercial", "ana.ferreira@" + dominioLimpo),
                new ContatoEncontrado("Bruno Costa", "Gerente de Projetos", "bruno.costa@" + dominioLimpo),
                new ContatoEncontrado("Carla Souza", "Coordenadora de Compras", "carla.souza@" + dominioLimpo)
        );
    }
}