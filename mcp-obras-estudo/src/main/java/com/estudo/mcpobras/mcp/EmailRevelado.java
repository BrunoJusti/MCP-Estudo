package com.estudo.mcpobras.mcp;

public record EmailRevelado(
        Long contatoId,
        String nome,
        String email,
        int creditosRestantes,
        boolean jaTinhaRevelado
) {
}