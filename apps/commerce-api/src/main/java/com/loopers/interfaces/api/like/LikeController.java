package com.loopers.interfaces.api.like;

import com.loopers.application.like.LikeFacade;
import com.loopers.interfaces.api.ApiResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/like")
public class LikeController implements LikeV1ApiSpec {
    private final LikeFacade likeFacade;

    @Override
    @PostMapping("/products/{productId}")
    public ApiResponse<Object> like(
            @RequestHeader("X-USER-ID") @NotNull(message = "X-USER-ID 헤더는 비어있을 수 없습니다.") String userId,
            @PathVariable Long productId) {
        this.likeFacade.like(userId, productId);
        return ApiResponse.success();
    }

    @Override
    @DeleteMapping("/products/{productId}")
    public ApiResponse<Object> unlike(
            @RequestHeader("X-USER-ID") @NotNull(message = "X-USER-ID 헤더는 비어있을 수 없습니다.") String userId,
            @PathVariable Long productId) {
        this.likeFacade.unlike(userId, productId);
        return ApiResponse.success();
    }
}
