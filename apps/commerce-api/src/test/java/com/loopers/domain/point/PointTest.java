package com.loopers.domain.point;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PointTest {

    @DisplayName("포인트 충전 시,")
    @Nested
    class Charge {
        @DisplayName("0 이하의 정수로 포인트를 충전 할 시, 실패한다.")
        @Test
        void charge_whenNegative() {
            // given
            Point point = Point.create("test");

            // when
            CoreException coreException = assertThrows(CoreException.class, () -> point.charge(-1));

            // then
            Assertions.assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }

    @DisplayName("포인트 사용 시,")
    @Nested
    class Use {
        @DisplayName("0 이하의 정수로 포인트를 사용 할 시, 실패한다.")
        @Test
        void use_whenNegative() {
            // given
            Point point = Point.create("test");

            // when
            CoreException coreException = assertThrows(CoreException.class, () -> point.use(-1));

            // then
            Assertions.assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("보유 포인트가 부족하면, 실패한다.")
        @Test
        void use_whenInsufficientPoint() {
            // given
            Point point = Point.create("test");
            point.charge(1000); // 현재 포인트 1000

            // when
            CoreException coreException = assertThrows(CoreException.class, () -> point.use(2000)); // 2000 사용 시도

            // then
            Assertions.assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            Assertions.assertThat(coreException.getMessage()).isEqualTo("보유 포인트가 부족합니다.");
        }

        @DisplayName("정상적인 요청이면, 포인트가 차감된다.")
        @Test
        void use_whenValidRequest() {
            // given
            Point point = Point.create("test");
            point.charge(2000); // 현재 포인트 2000

            // when
            point.use(1000); // 1000 사용

            // then
            Assertions.assertThat(point.getPoint()).isEqualTo(1000);
        }
    }
}
