package com.loopers.application.product;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.product.Product;

public record ProductWithBrandInfo(
        Long productId,
        String productName,
        int price,
        int stock,
        Long brandId,
        String brandName
) {

    public static ProductWithBrandInfo from(Product product, Brand brand) {
        return new ProductWithBrandInfo(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                brand.getId(),
                brand.getName()
        );
    }

}
