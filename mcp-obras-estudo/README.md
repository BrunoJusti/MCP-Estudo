# mcp-obras-estudo

Projeto de estudo: um servidor MCP em Spring Boot que expõe dados fictícios
de obras/empresas como *tools* que uma IA (Claude, ChatGPT etc.) pode chamar.

Serve como um protótipo reduzido do MCP que você quer construir de verdade
no ConstruConnect — mesma ideia (tools + escopo por empresa), só que com
dados de brinquedo em vez do banco real.

## Como rodar

Pré-requisitos: JDK 21 e Maven instalados (ou abrir o projeto direto numa
IDE como IntelliJ, que resolve isso sozinho).

```bash
mvn spring-boot:run
```

Na primeira subida, o `DataSeeder` popula o banco H2 em memória com 3
empresas e 6 obras de exemplo. O servidor MCP fica disponível em:

```
http://localhost:8080/mcp
```

O console do H2 (para conferir os dados direto) fica em
`http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:obras`, usuário
`sa`, senha em branco).

## Como testar sem precisar configurar um cliente de verdade

O jeito mais rápido de ver as tools funcionando é com o **MCP Inspector**
(ferramenta oficial da Anthropic para debugar servidores MCP):

```bash
npx @modelcontextprotocol/inspector
```

Ele abre uma UI no navegador onde você conecta em `http://localhost:8080/mcp`
(transporte Streamable HTTP), lista as tools descobertas automaticamente
(`buscar_obras`, `buscar_empresa_por_cnpj`, `listar_obras_por_empresa`) e
deixa você chamar cada uma manualmente, vendo o schema JSON gerado a partir
das anotações `@McpToolParam`.

## Tools disponíveis

| Tool | O que faz |
|---|---|
| `buscar_obras` | Lista obras, com filtro opcional de `segmento` e/ou `cidade` |
| `buscar_empresa_por_cnpj` | Retorna os dados de uma empresa a partir do CNPJ |
| `listar_obras_por_empresa` | Lista as obras de uma empresa específica (pelo id) |

## Próximos passos (se quiser ir além)

- **Conectar num cliente de verdade**: configurar o Claude Desktop ou outro
  cliente MCP para falar com esse servidor via Streamable HTTP.
- **Escopo automático por empresa**: hoje `listar_obras_por_empresa` recebe
  o id como parâmetro. No projeto real, esse id viria de um header
  (autenticação da conexão), lido via `McpTransportContext` num
  `contextExtractor` configurado no transporte MCP — isso simula o que
  o fluxo "Conectar Claude/ChatGPT" do ConstruConnect vai precisar fazer.
- **Trocar H2 por Postgres**: só muda o `application.yml` (datasource) e a
  dependência do driver — o código JPA continua igual.
