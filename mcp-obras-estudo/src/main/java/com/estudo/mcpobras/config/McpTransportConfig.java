package com.estudo.mcpobras.config;

import io.modelcontextprotocol.common.McpTransportContext;
import io.modelcontextprotocol.server.transport.WebMvcStreamableServerTransportProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class McpTransportConfig {

    @Bean
    public WebMvcStreamableServerTransportProvider webMvcStreamableServerTransportProvider() {
        return WebMvcStreamableServerTransportProvider.builder()
                .contextExtractor(serverRequest -> {
                    Map<String, Object> valores = new HashMap<>();

                    Authentication autenticacao = SecurityContextHolder.getContext().getAuthentication();
                    if (autenticacao instanceof JwtAuthenticationToken jwtAuthenticationToken) {
                        Object empresaId = jwtAuthenticationToken.getToken().getClaims().get("empresaId");
                        valores.put("empresaId", empresaId);
                    }

                    return McpTransportContext.create(valores);
                })
                .build();
    }
}