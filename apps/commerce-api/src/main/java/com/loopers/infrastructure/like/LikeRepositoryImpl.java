package com.loopers.infrastructure.like;

import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class LikeRepositoryImpl implements LikeRepository {
    private final LikeJpaRepository likeJpaRepository;

    @Override
    public Optional<Like> findByUserIdAndProductId(String userId, Long productId) {
        return this.likeJpaRepository.findByUserIdAndProductId(userId, productId);
    }

    @Override
    public boolean exists(Like like) {
        return this.likeJpaRepository.existsByUserIdAndProductId(like.getUserId(), like.getProductId());
    }

    @Override
    public Like save(Like like) {
        return this.likeJpaRepository.save(like);
    }

    @Override
    public void delete(Like like) {
        this.likeJpaRepository.delete(like);
    }

    @Override
    public Long countByProductId(Long productId) {
        return this.likeJpaRepository.countByProductId(productId);
    }
}
