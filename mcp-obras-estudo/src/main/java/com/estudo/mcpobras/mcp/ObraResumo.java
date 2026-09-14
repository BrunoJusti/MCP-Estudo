package com.estudo.mcpobras.mcp;

/**
 * Nunca devolvemos a entidade JPA diretamente pela tool MCP (mesmo motivo de
 * não devolver entidade direto num @RestController: evita vazar detalhes de
 * mapeamento/lazy loading e trava o formato de resposta que o modelo recebe).
 */
public record ObraResumo(
        Long id,
        String nome,
        String cidade,
        String segmento,
        String empresaRazaoSocial
) {
}
