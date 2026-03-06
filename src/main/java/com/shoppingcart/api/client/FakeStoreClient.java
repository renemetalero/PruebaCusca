package com.shoppingcart.api.client;

import com.shoppingcart.api.dto.ProductDto;
import com.shoppingcart.api.exception.ExternalServiceException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.util.Arrays;
import java.util.List;

@Component
public class FakeStoreClient {

    private final RestClient restClient;

    public FakeStoreClient(@Qualifier("fakeStoreRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public List<ProductDto> getAllProducts() {
        try {
            ProductDto[] response = restClient.get().uri("/products").retrieve().body(ProductDto[].class);
            return response == null ? List.of() : Arrays.asList(response);
        } catch (RestClientException ex) {
            throw new ExternalServiceException("Unable to fetch products from external service");
        }
    }

    public ProductDto getProductById(Long productId) {
        try {
            return restClient.get().uri("/products/{id}", productId).retrieve().body(ProductDto.class);
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                return null;
            }
            throw new ExternalServiceException("Unable to fetch product from external service");
        } catch (RestClientException ex) {
            throw new ExternalServiceException("Unable to fetch product from external service");
        }
    }
}
