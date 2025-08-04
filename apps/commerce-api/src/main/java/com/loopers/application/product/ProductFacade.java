package com.loopers.application.product;

import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductFacade {
    private final ProductService productService;

    public ProductResult getProduct(Long productId) {
        ProductInfo productInfo = this.productService.getProduct(productId);

    }

}
