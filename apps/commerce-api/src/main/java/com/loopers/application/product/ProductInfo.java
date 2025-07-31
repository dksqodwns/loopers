package com.loopers.application.product;

import com.loopers.domain.product.Product;

public record ProductInfo(Long id, Long brandId, String name, Integer price, Integer stock) {
    public static ProductInfo from(Product product) {
        return new ProductInfo(
                product.getId(),
                product.getBrandId(),
                product.getName(),
                product.getPrice(),
                product.getStock()
        );
    }
}
