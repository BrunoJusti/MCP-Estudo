# mcp-obras-estudo

Projeto de estudo: um servidor MCP em Spring Boot que expõe dados fictícios
de obras/empresas como *tools* que uma IA (Claude, ChatGPT etc.) pode chamar.

Serve como protótipo reduzido do MCP que vai ser construído de verdade no
ConstruConnect — mesma ideia (tools + escopo por empresa), com dados de
brinquedo em vez do banco real.

## Pré-requisitos

- **JDK 17** (o projeto está configurado para essa versão em `pom.xml`)
- **Maven** (ou abrir o projeto direto numa IDE como IntelliJ, que resolve
  isso sozinho)
- **Node.js 18+** — só é necessário para testar com o MCP Inspector e/ou
  conectar num cliente como o Claude Desktop

## Como rodar

Clone o repositório e, na raiz do projeto:

```bash
mvn compile     # baixa as dependências e compila
mvn spring-boot:run
```

Na primeira subida, o `DataSeeder` popula o banco H2 em memória com 3
empresas e 6 obras de exemplo. O servidor MCP fica disponível em:

http://localhost:8080/mcp


O console do H2 (para conferir os dados direto) fica em
`http://localhost:8080/h2-console` — JDBC URL: `jdbc:h2:mem:obras`,
usuário `sa`, senha em branco.

## Estrutura do projeto

src/main/java/com/estudo/mcpobras/
├── McpObrasApplication.java # ponto de entrada Spring Boot
├── domain/ # entidades JPA (Empresa, Obra)
├── repository/ # interfaces Spring Data JPA
├── mcp/ # tools MCP (@McpTool) + DTOs de resposta
└── config/ # DataSeeder (popula dados de exemplo)


## Tools disponíveis

| Tool | O que faz |
|---|---|
| `buscar_obras` | Lista obras, com filtro opcional de `segmento` e/ou `cidade` |
| `buscar_empresa_por_cnpj` | Retorna os dados de uma empresa a partir do CNPJ |
| `listar_obras_por_empresa` | Lista as obras de uma empresa específica (pelo id) |

## Testando com o MCP Inspector

Sem precisar plugar num cliente de verdade, o jeito mais rápido de ver as
tools funcionando é com o **MCP Inspector** (ferramenta oficial da
Anthropic para debugar servidores MCP):

```bash
npx @modelcontextprotocol/inspector
```

Isso abre uma UI no navegador (`http://localhost:6274` por padrão). Na
aba **Servers**, conecte usando transporte **Streamable HTTP** na URL
`http://localhost:8080/mcp`. Depois, na aba **Tools**, as três tools
aparecem automaticamente com o schema gerado a partir das anotações
`@McpToolParam` — dá pra chamar cada uma manualmente preenchendo os
parâmetros.

## Conectando no Claude Desktop

O Claude Desktop não aceita uma URL `http://localhost` direto na tela de
"Add custom connector" (isso é para servidores públicos/hospedados). Para
um servidor local, o caminho é editar o arquivo de configuração usando o
`mcp-remote` como ponte entre o Claude Desktop e o Streamable HTTP do
servidor:

1. Deixe a aplicação rodando (`mvn spring-boot:run`)
2. No Claude Desktop: **Settings → Developer → Edit Config** (abre o
   `claude_desktop_config.json`; no Windows fica em
   `%APPDATA%\Claude\claude_desktop_config.json`)
3. Adicione a entrada abaixo em `mcpServers` (mantendo outras entradas
   que já existirem):

```json
   {
     "mcpServers": {
       "mcp-obras-estudo": {
         "command": "npx",
         "args": [
           "mcp-remote",
           "http://localhost:8080/mcp",
           "--allow-http"
         ]
       }
     }
   }
```

O `--allow-http` é necessário porque o servidor roda em `http://` puro
(sem TLS) — por padrão o `mcp-remote` exige `https://` para endereços
remotos.

4. Feche o Claude Desktop por completo (não só a janela) e abra de novo
5. Confira nas configurações de conectores se o `mcp-obras-estudo`
   aparece conectado com as 3 tools
6. Teste com uma pergunta em linguagem natural, ex: *"quais obras a
   Construtora Alfa tem em Ribeirão Preto?"*

## Próximos passos (se quiser ir além)

- **Hints corretas nas tools**: adicionar
  `annotations = @McpTool.McpAnnotations(readOnlyHint = true, destructiveHint = false)`
  no `@McpTool`, já que essas tools só leem dados
- **Escopo automático por empresa**: hoje `listar_obras_por_empresa`
  recebe o id como parâmetro. No projeto real, esse id viria de um
  header (autenticação da conexão), lido via `McpTransportContext` num
  `contextExtractor` configurado no transporte MCP — simula o que o
  fluxo "Conectar Claude/ChatGPT" do ConstruConnect vai precisar fazer
- **Trocar H2 por Postgres**: só muda o `application.yml` (datasource) e
  a dependência do driver — o código JPA continua igual
