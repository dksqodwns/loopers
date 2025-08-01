package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductInfo;
import com.loopers.application.product.ProductInfo.ProductListInfo;
import java.util.List;

public class ProductDto {
    public record V1() {
        public record ProductResponse(Long id, Long brandId, String name, int price, int stock, String brandName, Long likeCount) {
            public static ProductResponse from(ProductInfo productInfo) {
                return new ProductResponse(
                        productInfo.id(),
                        productInfo.brandId(),
                        productInfo.name(),
                        productInfo.price(),
                        productInfo.stock(),
                        productInfo.brandName(),
                        productInfo.likeCount()
                );
            }
        }

        public record ProductListResponse(List<ProductResponse> productList, Integer pageSize) {
            public static ProductListResponse from(ProductListInfo productListInfo) {
                return new ProductListResponse(
                        productListInfo.productList().map(ProductResponse::from).toList(),
                        productListInfo.productList().getTotalPages()
                );
            }
        }
    }
}
