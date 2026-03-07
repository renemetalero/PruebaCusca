package com.shoppingcart.api.controller;

import com.shoppingcart.api.dto.ProductDto;
import com.shoppingcart.api.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<ProductDto>> getProducts() {
        log.info("[ProductController] getProducts");
        return ResponseEntity.ok(productService.getProducts());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        log.info("[ProductController] getProduct id={}", id);
        return ResponseEntity.ok(productService.getProduct(id));
    }
}
