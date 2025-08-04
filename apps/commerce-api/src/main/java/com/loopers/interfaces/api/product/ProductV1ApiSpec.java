package com.loopers.interfaces.api.product;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product V1 API", description = "상품 API")
public interface ProductV1ApiSpec {
    @Operation(
            summary = "상품 정보 조회"
    )
    ApiResponse<ProductDto.V1.ProductResponse> getProduct(
            @Schema(name = "상품 ID")
            Long productId
    );

    @Operation(
            summary = "상품 목록 조회"
    )
    ApiResponse<ProductDto.V1.ProductsResponse> getProducts(
            @Schema(name = "페이지")
            int page,

            @Schema(name = "사이즈")
            int size,

            @Schema(name = "정렬필드:값")
            String sortParam
    );
}
