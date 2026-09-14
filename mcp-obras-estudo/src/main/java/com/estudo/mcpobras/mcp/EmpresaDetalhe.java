package com.estudo.mcpobras.mcp;

public record EmpresaDetalhe(
        Long id,
        String cnpj,
        String razaoSocial,
        String segmento
) {
}
