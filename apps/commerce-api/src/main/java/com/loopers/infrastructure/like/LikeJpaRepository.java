package com.loopers.infrastructure.like;

import com.loopers.domain.like.Like;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeJpaRepository extends JpaRepository<Like, Long> {
    long countByProductId(Long productId);

    boolean existsByUserIdAndProductId(String userId, Long productId);

    Optional<Like> findByUserIdAndProductId(String userId, Long productId);
}
