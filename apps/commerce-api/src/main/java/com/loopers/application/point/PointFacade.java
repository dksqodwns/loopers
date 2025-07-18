package com.loopers.application.point;

import com.loopers.application.point.PointCommand.PointChargeCommand;
import com.loopers.domain.point.PointModel;
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

    public PointInfo chargePoint(PointChargeCommand command) {
        PointModel chargedPointModel = pointService.charge(command.userId(), command.point());

        return PointInfo.from(chargedPointModel);
    }

    public PointInfo getPoints(String userId) {
        PointModel pointModel;

        userService.getUserByUserId(userId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "유저를 찾을 수 없습니다."));

        pointModel = pointService.getPoints(userId);
        if (pointModel == null) {
            pointModel = PointModel.create(userId);
        }

        return PointInfo.from(pointModel);
    }
}
