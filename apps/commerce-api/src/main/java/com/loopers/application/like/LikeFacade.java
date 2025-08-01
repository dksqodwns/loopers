package com.loopers.application.like;

import com.loopers.domain.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LikeFacade {

    private final LikeService likeService;

    public void like(String userId, Long productId) {
        LikeCommand.Like command = new LikeCommand.Like(userId, productId);
        this.likeService.like(command);
    }

    public void unlike(String userId, Long productId) {
        this.likeService.unlike(userId, productId);
    }
}
