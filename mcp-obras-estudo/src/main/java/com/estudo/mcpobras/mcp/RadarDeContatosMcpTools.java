package com.estudo.mcpobras.mcp;

import com.estudo.mcpobras.domain.Contato;
import com.estudo.mcpobras.domain.Empresa;
import com.estudo.mcpobras.domain.RevelacaoContato;
import com.estudo.mcpobras.externo.ContatoEncontrado;
import com.estudo.mcpobras.externo.SnovIoSimulado;
import com.estudo.mcpobras.repository.ContatoRepository;
import com.estudo.mcpobras.repository.EmpresaRepository;
import com.estudo.mcpobras.repository.RevelacaoContatoRepository;
import io.modelcontextprotocol.common.McpTransportContext;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RadarDeContatosMcpTools {

    private final EmpresaRepository empresaRepository;
    private final ContatoRepository contatoRepository;
    private final RevelacaoContatoRepository revelacaoContatoRepository;
    private final SnovIoSimulado snovIoSimulado;

    public RadarDeContatosMcpTools(
            EmpresaRepository empresaRepository,
            ContatoRepository contatoRepository,
            RevelacaoContatoRepository revelacaoContatoRepository,
            SnovIoSimulado snovIoSimulado) {
        this.empresaRepository = empresaRepository;
        this.contatoRepository = contatoRepository;
        this.revelacaoContatoRepository = revelacaoContatoRepository;
        this.snovIoSimulado = snovIoSimulado;
    }

    @McpTool(
            name = "buscar_contatos_por_dominio",
            description = "Busca contatos (nome e cargo) associados a um domínio (ex: exemplo.com.br). " +
                    "Sempre gratuito, não consome crédito. Não devolve o e-mail dos contatos - para isso, " +
                    "use revelar_email_contato com o id de um contato específico retornado aqui.",
            annotations = @McpTool.McpAnnotations(
                    readOnlyHint = false,
                    destructiveHint = false,
                    idempotentHint = false,
                    openWorldHint = true
            )
    )
    public List<ContatoResumo> buscarContatosPorDominio(
            McpTransportContext transportContext,
            @McpToolParam(description = "Domínio do site da empresa alvo, ex: exemplo.com.br", required = true)
            String dominio) {

        Empresa empresa = resolverEmpresaAutenticada(transportContext);

        List<Contato> contatos = contatoRepository.findByDominioIgnoreCase(dominio);

        if (contatos.isEmpty()) {
            List<ContatoEncontrado> encontrados = snovIoSimulado.buscarContatosPorDominio(dominio);
            contatos = encontrados.stream()
                    .map(c -> contatoRepository.save(new Contato(c.nome(), c.cargo(), c.email(), dominio)))
                    .toList();
        }

        return contatos.stream()
                .map(contato -> new ContatoResumo(
                        contato.getId(),
                        contato.getNome(),
                        contato.getCargo(),
                        revelacaoContatoRepository
                                .findByEmpresaIdAndContatoId(empresa.getId(), contato.getId())
                                .isPresent()))
                .toList();
    }

    @McpTool(
            name = "revelar_email_contato",
            description = "Revela o e-mail de UM contato específico (pelo id retornado por " +
                    "buscar_contatos_por_dominio). Consome 1 crédito da empresa autenticada nesta conexão, " +
                    "a não ser que essa empresa já tenha revelado esse mesmo contato antes - nesse caso é " +
                    "gratuito. Retorna erro se a empresa não tiver créditos.",
            annotations = @McpTool.McpAnnotations(
                    readOnlyHint = false,
                    destructiveHint = false,
                    idempotentHint = false,
                    openWorldHint = false
            )
    )
    public EmailRevelado revelarEmailContato(
            McpTransportContext transportContext,
            @McpToolParam(description = "Id do contato, obtido em buscar_contatos_por_dominio", required = true)
            Long contatoId) {

        Empresa empresa = resolverEmpresaAutenticada(transportContext);

        Contato contato = contatoRepository.findById(contatoId)
                .orElseThrow(() -> new IllegalArgumentException("Nenhum contato encontrado com id " + contatoId));

        Optional<RevelacaoContato> jaRevelado =
                revelacaoContatoRepository.findByEmpresaIdAndContatoId(empresa.getId(), contatoId);

        if (jaRevelado.isPresent()) {
            return new EmailRevelado(
                    contato.getId(), contato.getNome(), contato.getEmail(), empresa.getCreditosEmailRadar(), true);
        }

        if (empresa.getCreditosEmailRadar() == null || empresa.getCreditosEmailRadar() <= 0) {
            throw new IllegalStateException(
                    "A empresa " + empresa.getRazaoSocial() + " não possui créditos disponíveis para revelar contatos.");
        }

        empresa.setCreditosEmailRadar(empresa.getCreditosEmailRadar() - 1);
        empresaRepository.save(empresa);
        revelacaoContatoRepository.save(new RevelacaoContato(empresa, contato));

        return new EmailRevelado(
                contato.getId(), contato.getNome(), contato.getEmail(), empresa.getCreditosEmailRadar(), false);
    }

    private Empresa resolverEmpresaAutenticada(McpTransportContext transportContext) {
        Object empresaIdClaim = transportContext.get("empresaId");

        if (!(empresaIdClaim instanceof Number numero)) {
            throw new IllegalStateException(
                    "Conexão sem token OAuth válido - não foi possível identificar a empresa.");
        }

        Long empresaId = numero.longValue();

        return empresaRepository.findById(empresaId)
                .orElseThrow(() -> new IllegalStateException(
                        "Nenhuma empresa encontrada para o id do token: " + empresaId));
    }
}