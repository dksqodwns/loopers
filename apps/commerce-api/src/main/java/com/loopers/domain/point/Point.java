package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "points")
public class Point extends BaseEntity {
    private String userId;
    private int point;

    private Point(String userId, int point) {
        this.userId = userId;
        this.point = point;
    }

    public static Point create(String userId) {
        return new Point(userId, 0);
    }

    public Point charge(int chargePoint) {
        if (chargePoint < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "0이하의 정수는 충전 할 수 없습니다.");
        }
        this.point += chargePoint;
        return this;
    }
}
