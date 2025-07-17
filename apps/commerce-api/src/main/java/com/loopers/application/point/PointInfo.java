package com.loopers.application.point;

import com.loopers.domain.point.PointModel;

public record PointInfo(Long id, String userId, int point) {
    public static PointInfo from(PointModel pointModel) {
        return new PointInfo(
                pointModel.getId(),
                pointModel.getUserId(),
                pointModel.getPoint()
        );
    }
}
