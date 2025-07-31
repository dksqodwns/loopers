package com.loopers.domain.like;

import java.util.Optional;

public interface LikeRepository {
    Like save(Like like);

    void delete(Like like);

    boolean exists(Like like);

    Optional<Like> findByUserIdAndProductId(String userId, Long productId);

    Long countByProductId(Long productId);
}
