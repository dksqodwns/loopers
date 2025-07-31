package com.loopers.domain.like;

import com.loopers.application.like.LikeCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;

    @Transactional
    public void like(LikeCommand.Like command) {
        Like like = Like.create(command);
        if (this.likeRepository.exists(like)) {
            return;
        }

        likeRepository.save(like);
    }

    @Transactional
    public void unlike(String userId, Long productId) {
        this.likeRepository.findByUserIdAndProductId(userId, productId)
                .ifPresent(likeRepository::delete);
    }
}
