package com.backend.srv_order.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OpenApiConfigTest {
    
    @Test
    public void testCustomOpenAPI() {
        OpenApiConfig openApiConfig = new OpenApiConfig();
        OpenAPI openAPI = openApiConfig.customOpenAPI();
        
        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("Order Management API", openAPI.getInfo().getTitle());
        assertEquals("API para gerenciamento de pedidos", openAPI.getInfo().getDescription());
        assertEquals("1.0.0", openAPI.getInfo().getVersion());
        
        assertNotNull(openAPI.getInfo().getLicense());
        assertEquals("Apache 2.0", openAPI.getInfo().getLicense().getName());
        assertEquals("https://springdoc.org", openAPI.getInfo().getLicense().getUrl());
        
        assertNotNull(openAPI.getExternalDocs());
        assertEquals("Documentação Completa", openAPI.getExternalDocs().getDescription());
        assertEquals("https://example.com/docs", openAPI.getExternalDocs().getUrl());
    }
}
