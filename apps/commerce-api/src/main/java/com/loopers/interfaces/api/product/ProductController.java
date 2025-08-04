package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductFacade;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController implements ProductV1ApiSpec {

    private final ProductFacade productFacade;

    @Override
    public ApiResponse<ProductDto.V1.ProductResponse> getProduct(Long productId) {

        return ApiResponse.success();
    }

    @Override
    public ApiResponse<ProductDto.V1.ProductsResponse> getProducts(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sortParam
    ) {

        return ApiResponse.success();
    }
}
