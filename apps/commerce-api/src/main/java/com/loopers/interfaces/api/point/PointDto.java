package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointCommand.Charge;
import com.loopers.application.point.PointInfo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class PointDto {
    public record PointResponse(int point) {
        public static PointResponse from(PointInfo pointInfo) {
            return new PointResponse(
                    pointInfo.point()
            );
        }
    }

    public record PointChargeRequest(
            @NotNull(message = "충전 포인트는 비어 있을 수 없습니다.")
            @PositiveOrZero
            int point
    ) {
        public Charge toCommand(String userId) {
            return new Charge(userId, point);
        }
    }

    
}
