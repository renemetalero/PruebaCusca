package com.shoppingcart.api.service;

import com.shoppingcart.api.client.FakeStoreClient;
import com.shoppingcart.api.dto.ProductDto;
import com.shoppingcart.api.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final FakeStoreClient fakeStoreClient;

    public List<ProductDto> getProducts() {
        return fakeStoreClient.getAllProducts();
    }

    public ProductDto getProduct(Long id) {
        ProductDto product = fakeStoreClient.getProductById(id);
        if (product == null || product.id() == null) {
            throw new NotFoundException("Product not found");
        }
        return product;
    }
}
