package com.shoppingcart.api.dto;

import java.math.BigDecimal;

public record ProductDto(Long id, String title, BigDecimal price, String description, String category, String image) {
}
