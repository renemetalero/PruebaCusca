package com.shoppingcart.api.service;

import com.shoppingcart.api.client.FakeStoreClient;
import com.shoppingcart.api.dto.ProductDto;
import com.shoppingcart.api.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private FakeStoreClient fakeStoreClient;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldReturnProducts() {
        when(fakeStoreClient.getAllProducts()).thenReturn(List.of(new ProductDto(1L, "P", BigDecimal.ONE, "d", "c", "i")));

        List<ProductDto> result = productService.getProducts();

        assertEquals(1, result.size());
    }

    @Test
    void shouldThrowWhenProductNotFound() {
        when(fakeStoreClient.getProductById(1L)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> productService.getProduct(1L));
    }
}
