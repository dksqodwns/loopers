package com.loopers.domain.point;

import com.loopers.domain.user.UserRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Component
public class PointService {
    private final UserRepository userRepository;
    private final PointRepository pointRepository;

    @Transactional
    public PointModel charge(String userId, int chargePoint) {
        userRepository.findByUserId(userId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "유저를 찾을 수 없습니다."));

        PointModel pointModel = pointRepository.findByUserId(userId)
                .orElseGet(() -> PointModel.create(userId));
        PointModel chargedPointModel = pointModel.charge(chargePoint);

        return this.pointRepository.save(chargedPointModel);
    }

    @Transactional(readOnly = true)
    public PointModel getPoints(String userId) {
        return userRepository.findByUserId(userId)
                .map(user -> pointRepository
                        .findByUserId(user.getUserId())
                        .orElseGet(() -> pointRepository.save(PointModel.create(userId))))
                .orElse(null);
    }

}
