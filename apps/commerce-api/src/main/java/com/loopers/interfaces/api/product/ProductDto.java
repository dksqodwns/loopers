package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductWithBrandInfo;

public class ProductDto {
    public record V1() {
        public record ProductResponse(Long id, Long brandId, String name, int price, int stock, String brandName, Long likeCount) {
            public static ProductResponse from(ProductWithBrandInfo productInfo) {
                return new ProductResponse(
                        productInfo.productId(),
                        productInfo.brandId(),
                        productInfo.productName(),
                        productInfo.price(),
                        productInfo.stock(),
                        productInfo.brandName(),
                        productInfo.likeCount()
                );
            }
        }
    }
}
