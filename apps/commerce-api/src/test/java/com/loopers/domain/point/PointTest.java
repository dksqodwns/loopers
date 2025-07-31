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
}
