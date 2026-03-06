package com.shoppingcart.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestClientConfigTest {

    @Test
    void shouldCreateRestClientBean() {
        RestClientConfig config = new RestClientConfig();
        RestClient client = config.fakeStoreRestClient("https://fakestoreapi.com");

        assertNotNull(client);
    }
}
