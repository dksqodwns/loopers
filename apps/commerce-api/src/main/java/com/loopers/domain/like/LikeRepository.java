package com.loopers.domain.like;

import java.util.List;
import java.util.Optional;

public interface LikeRepository {

    boolean existsBy(Long userId, LikeTarget likeTarget);

    void save(Like like);

    Optional<Like> findBy(Long userId, LikeTarget likeTarget);

    void delete(Like like);

    List<Like> findAllBy(Long userId, LikeTarget.TargetType targetType);
}
