package com.shoppingcart.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OpenApiConfigTest {

    @Test
    void shouldCreateOpenApiWithBearerScheme() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.customOpenApi();

        assertNotNull(openAPI.getComponents().getSecuritySchemes().get("bearerAuth"));
        assertEquals("Shopping Cart API", openAPI.getInfo().getTitle());
    }
}
