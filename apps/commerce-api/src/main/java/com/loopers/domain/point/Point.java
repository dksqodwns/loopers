package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
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
    @Column(name = "ref_user_id")
    private String userId;
    private Integer point;

    private Point(String userId, Integer point) {
        this.userId = userId;
        this.point = point;
    }

    public static Point create(String userId) {
        return new Point(userId, 0);
    }

    public Point charge(Integer chargePoint) {
        if (chargePoint < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "0이하의 정수는 충전 할 수 없습니다.");
        }
        this.point += chargePoint;
        return this;
    }

    public Point use(Integer usePoint) {
        if (usePoint < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "0 이하의 정수는 사용 할 수 없습니다.");
        }

        if (this.point < usePoint) {
            throw new CoreException(ErrorType.BAD_REQUEST, "보유 포인트가 부족합니다.");
        }

        this.point -= usePoint;
        return this;
    }
}
