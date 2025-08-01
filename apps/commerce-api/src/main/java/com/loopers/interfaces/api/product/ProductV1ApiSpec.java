package com.loopers.interfaces.api.product;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.product.ProductDto.V1.ProductListResponse;
import com.loopers.interfaces.api.product.ProductDto.V1.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;

@Tag(name = "Product V1 API", description = "상품 API 입니다.")
public interface ProductV1ApiSpec {
    @Operation(summary = "상품 상세 조회")
    ApiResponse<ProductResponse> getProductById(
            @Schema
            Long productId
    );

    @Operation(summary = "상품 목록 조회")
    ApiResponse<ProductListResponse> getProductList(
            @Parameter(description = "정렬 필드:기준")
            String sort,
            @Parameter
            Pageable pageable
    );
}
