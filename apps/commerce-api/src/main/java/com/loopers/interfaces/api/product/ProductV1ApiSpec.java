package com.loopers.interfaces.api.product;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.product.ProductDto.V1.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "Product V1 API", description = "상품 API 입니다.")
public interface ProductV1ApiSpec {

    @Operation(summary = "상품 전체 조회")
    ApiResponse<List<ProductResponse>> getProducts();

    @Operation(summary = "상품 상세 조회")
    ApiResponse<ProductResponse> getProductById(
            @Schema
            Long productId
    );
}
