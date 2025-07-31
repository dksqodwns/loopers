package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductInfo;

public class ProductDto {
    class V1 {
        public record ProductResponse(Long id, Long brandId, String name, int price, int stock, int likeCount) {
            public static ProductResponse from(ProductInfo productInfo) {
                return new ProductResponse(
                        productInfo.id(),
                        productInfo.brandId(),
                        productInfo.name(),
                        productInfo.price(),
                        productInfo.stock(),
                        productInfo.likeCount()
                );
            }
        }

    }
}
