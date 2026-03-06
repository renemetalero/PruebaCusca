package com.shoppingcart.api.client;

import com.shoppingcart.api.dto.ProductDto;
import com.shoppingcart.api.exception.ExternalServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FakeStoreClientTest {

    @Mock
    private RestClient restClient;
    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private RestClient.ResponseSpec responseSpec;

    @Test
    void shouldReturnProducts() {
        ProductDto[] products = {new ProductDto(1L, "P", BigDecimal.ONE, "d", "c", "i")};
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/products")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(ProductDto[].class)).thenReturn(products);

        FakeStoreClient client = new FakeStoreClient(restClient);
        List<ProductDto> result = client.getAllProducts();

        assertEquals(1, result.size());
    }

    @Test
    void shouldThrowExternalServiceExceptionOnFailure() {
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/products")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenThrow(new RestClientException("fail"));

        FakeStoreClient client = new FakeStoreClient(restClient);

        assertThrows(ExternalServiceException.class, client::getAllProducts);
    }
}
