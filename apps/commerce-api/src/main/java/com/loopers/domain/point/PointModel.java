package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "points")
public class PointModel extends BaseEntity {
    private String userId;
    private int point;

    private PointModel(String userId, int point) {
        this.userId = userId;
        this.point = point;
    }

    public static PointModel create(String userId) {
        return new PointModel(userId, 0);
    }

    public PointModel charge(int chargePoint) {
        this.point += chargePoint;
        return this;
    }
}
