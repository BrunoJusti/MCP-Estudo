package com.estudo.mcpobras.mcp;

import com.estudo.mcpobras.domain.Empresa;
import com.estudo.mcpobras.domain.Obra;
import com.estudo.mcpobras.repository.EmpresaRepository;
import com.estudo.mcpobras.repository.ObraRepository;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Cada método anotado com @McpTool vira uma "tool" que o cliente MCP (Claude,
 * ChatGPT, MCP Inspector etc.) descobre automaticamente ao conectar - sem
 * precisar registrar nada manualmente. A descrição em @McpTool/@McpToolParam
 * é o que o modelo lê para decidir quando e como chamar cada tool, então
 * pense nela como documentação para a IA, não só um comentário para pessoas.
 */
@Service
public class ObraMcpTools {

    private final ObraRepository obraRepository;
    private final EmpresaRepository empresaRepository;

    public ObraMcpTools(ObraRepository obraRepository, EmpresaRepository empresaRepository) {
        this.obraRepository = obraRepository;
        this.empresaRepository = empresaRepository;
    }

    @McpTool(
            name = "buscar_obras",
            description = "Busca obras cadastradas, filtrando opcionalmente por segmento e/ou cidade. " +
                    "Se nenhum filtro for informado, retorna todas as obras."
    )
    public List<ObraResumo> buscarObras(
            @McpToolParam(description = "Segmento da obra, ex: Residencial, Comercial, Industrial", required = false)
            String segmento,
            @McpToolParam(description = "Cidade onde a obra está localizada", required = false)
            String cidade) {

        return obraRepository.findAll().stream()
                .filter(obra -> segmento == null || obra.getSegmento().equalsIgnoreCase(segmento))
                .filter(obra -> cidade == null || obra.getCidade().equalsIgnoreCase(cidade))
                .map(this::paraResumo)
                .toList();
    }

    @McpTool(
            name = "buscar_empresa_por_cnpj",
            description = "Busca os dados cadastrais de uma empresa a partir do CNPJ"
    )
    public EmpresaDetalhe buscarEmpresaPorCnpj(
            @McpToolParam(description = "CNPJ da empresa, com ou sem formatação", required = true)
            String cnpj) {

        Empresa empresa = empresaRepository.findByCnpj(cnpj)
                // uma RuntimeException aqui é reportada ao modelo como erro da
                // tool call, permitindo que ele reaja (ex: avisar o usuário),
                // em vez de quebrar a conexão MCP inteira
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nenhuma empresa encontrada para o CNPJ " + cnpj));

        return new EmpresaDetalhe(empresa.getId(), empresa.getCnpj(), empresa.getRazaoSocial(), empresa.getSegmento());
    }

    @McpTool(
            name = "listar_obras_por_empresa",
            description = "Lista as obras que pertencem a uma empresa específica, dado o id da empresa. " +
                    "Serve como base para, no futuro, restringir a resposta apenas à empresa autenticada na conexão."
    )
    public List<ObraResumo> listarObrasPorEmpresa(
            @McpToolParam(description = "Id da empresa", required = true)
            Long empresaId) {

        return obraRepository.findByEmpresaId(empresaId).stream()
                .map(this::paraResumo)
                .toList();
    }

    private ObraResumo paraResumo(Obra obra) {
        return new ObraResumo(
                obra.getId(),
                obra.getNome(),
                obra.getCidade(),
                obra.getSegmento(),
                obra.getEmpresa().getRazaoSocial());
    }
}
