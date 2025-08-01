package com.loopers.application.product;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.product.Product;
import org.springframework.data.domain.Page;

public record ProductInfo(Long id, Long brandId, String name, Integer price, Integer stock, String brandName,
                          Long likeCount) {
    public static ProductInfo from(Product product) {
        return new ProductInfo(
                product.getId(),
                product.getBrandId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                null,
                null
        );
    }

    public static ProductInfo from(Product product, Brand brand, Long likeCount) {
        return new ProductInfo(
                product.getId(),
                product.getBrandId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                brand.getName(),
                likeCount
        );
    }

    public record ProductListInfo(Page<ProductInfo> productList) {
        public static ProductListInfo from(Page<ProductInfo> productList) {
            Page<ProductInfo> productListInfo = (
                    productList.map(
                            product -> new ProductInfo(
                                    product.id(), product.brandId, product.name, product.price, product.stock,
                                    product.brandName, product.likeCount
                            )
                    )
            );

            return new ProductListInfo(productListInfo);
        }
    }
}
