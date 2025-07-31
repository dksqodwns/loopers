package com.loopers.interfaces.api.product;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.product.ProductDto.V1.ProductResponse;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/products")
@RestController
@RequiredArgsConstructor

public class ProductController implements ProductV1ApiSpec {

    @Override
    @GetMapping("")
    public ApiResponse<List<ProductResponse>> getProducts() {
        List<ProductResponse> result = Collections.singletonList(new ProductResponse(1L, 1L, "테스트 상품1", 1_000, 10, 0));

        return ApiResponse.success(result);
    }

    @Override
    @GetMapping("/{productId}")
    public ApiResponse<ProductDto.V1.ProductResponse> getProductById(@PathVariable Long productId) {
        ProductResponse reuslt = new ProductResponse(1L, 1L, "테스트 상품1", 1_000, 10, 0);
        return ApiResponse.success(reuslt);
    }
}
