package com.loopers.application.product;

import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.stock.ProductStockInfo;

public record ProductResult(
        Long id,
        String name,
        Long price,
        Integer stock,
        Long brnadId,
        String brandName,
        Long likeCount
) {
    public static ProductResult of(
            final ProductInfo productInfo,
            final BrandInfo brandInfo,
            final ProductStockInfo productStockInfo
            final ProductCountInfo productCountInfo
    ) {
        return new ProductResult(
                productInfo.id(),
                productInfo.name(),
                productInfo.price(),
                productStockInfo.map(ProductStockInfo::from).orElse(null),
                productCountInfo.map(ProductCountInfo::from).orElse(null),
                brandInfo.id(),
                brandInfo.name()
        );
    }
}
