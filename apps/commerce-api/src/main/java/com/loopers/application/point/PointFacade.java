package com.loopers.application.point;

import com.loopers.domain.point.PointInfo;
import com.loopers.domain.point.PointService;
import com.loopers.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PointFacade {

    private final UserService userService;
    private final PointService pointService;

    public PointResult charge(PointCriteria.Charge criteria) {
        PointInfo chargedPoint = pointService.charge(criteria.toCommand());
        return PointResult.from(chargedPoint);
    }

    public PointResult getPoint(Long userId) {
        PointInfo pointInfo = pointService.getPoint(userId);
        return PointResult.from(pointInfo);
    }

}
