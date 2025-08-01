package com.loopers.interfaces.api.like;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Like V1 API", description = "좋아요 API 입니다.")
public interface LikeV1ApiSpec {
    @Operation(summary = "좋아요 등록")
    ApiResponse<Object> like(
            @Schema(name = "유저 PK")
            String userId,

            @Schema(name = "상품 PK")
            Long productId
    );

    @Operation(summary = "좋아요 취소")
    ApiResponse<Object> unlike(
            @Schema(name = "유저 PK")
            String userId,

            @Schema(name = "상품 PK")
            Long productId
    );
}
