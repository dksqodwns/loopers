package com.loopers.application.point;

import com.loopers.application.point.PointCommand.Charge;
import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointService;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PointFacade {
    private final UserService userService;
    private final PointService pointService;

    public PointInfo chargePoint(Charge command) {
        Point chargedPoint = pointService.charge(command);
        return PointInfo.from(chargedPoint);
    }

    public PointInfo getPoints(String userId) {
        Point point;

        userService.getUserByUserId(userId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "유저를 찾을 수 없습니다."));

        point = pointService.getPoints(userId);
        if (point == null) {
            point = Point.create(userId);
        }

        return PointInfo.from(point);
    }
}
