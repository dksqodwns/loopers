package com.loopers.infrastructure.point;

import com.loopers.domain.point.PointModel;
import com.loopers.domain.point.PointRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PointRepositoryImpl implements PointRepository {
    private final PointJpaRepository pointJpaRepository;

    @Override
    public PointModel save(PointModel pointModel) {
        return this.pointJpaRepository.save(pointModel);
    }

    @Override
    public Optional<PointModel> findByUserId(String userId) {
        return this.pointJpaRepository.findByUserId(userId);
    }
}
