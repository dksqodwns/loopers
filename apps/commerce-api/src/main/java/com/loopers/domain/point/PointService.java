package com.loopers.domain.point;

import com.loopers.application.point.PointCommand;
import com.loopers.application.point.PointCommand.Charge;
import com.loopers.application.point.PointInfo;
import com.loopers.domain.user.UserRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class PointService {
    private final UserRepository userRepository;
    private final PointRepository pointRepository;

    @Transactional
    public Point charge(Charge command) {
        userRepository.findByUserId(command.userId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "유저를 찾을 수 없습니다."));

        Point point = pointRepository.findByUserId(command.userId())
                .orElseGet(() -> Point.create(command.userId()));
        Point chargedPoint = point.charge(command.point());

        return this.pointRepository.save(chargedPoint);
    }

    @Transactional
    public Point getPoints(String userId) {
        return userRepository.findByUserId(userId)
                .map(user -> pointRepository
                        .findByUserId(user.getUserId())
                        .orElseGet(() -> pointRepository.save(Point.create(userId))))
                .orElse(null);
    }

    public PointInfo use(PointCommand.Use command) {
        Point point = this.pointRepository.findByUserId(command.userId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "회원을 찾을 수 없습니다. userId: " + command.userId()));

        Point usedPoint = point.use(command.usePoint());
        return PointInfo.from(this.pointRepository.save(usedPoint));
    }

}
