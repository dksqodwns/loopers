package com.loopers.domain.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.loopers.application.point.PointCommand;
import com.loopers.application.point.PointInfo;
import com.loopers.domain.user.UserRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @InjectMocks
    private PointService pointService;

    @Mock
    private PointRepository mockPointRepository;

    @Mock
    private UserRepository mockUserRepository;

    @DisplayName("포인트 사용 시,")
    @Nested
    class UsePoint {

        @DisplayName("보유 포인트가 부족하면, CoreException을 반환한다.")
        @Test
        void throwCoreException_whenPointIsInsufficient() {
            // given
            String userId = "testUser";
            Integer currentPoint = 1000;
            Integer usePoint = 2000;
            PointCommand.Use command = new PointCommand.Use(userId, usePoint);

            Point point = Point.create(userId);
            point.charge(currentPoint);

            given(mockPointRepository.findByUserId(userId)).willReturn(Optional.of(point));

            CoreException coreException = assertThrows(CoreException.class, () -> pointService.use(command));

            assertAll(
                    () ->
                            assertThatThrownBy(() -> pointService.use(command))
                                    .isInstanceOf(CoreException.class)
                                    .hasMessageContaining("보유 포인트가 부족합니다."),
                    () -> assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)
            );


        }

        @DisplayName("정상적인 요청이면, 포인트가 차감된다.")
        @Test
        void decreasePoint_whenValidRequest() {
            // given
            String userId = "testUser";
            Integer currentPoint = 2000;
            Integer usePoint = 1000;
            PointCommand.Use command = new PointCommand.Use(userId, usePoint);

            Point point = Point.create(userId);
            point.charge(currentPoint);

            given(mockPointRepository.findByUserId(userId)).willReturn(Optional.of(point));
            given(mockPointRepository.save(any(Point.class))).willAnswer(invocation -> invocation.getArgument(0));

            // when
            PointInfo pointInfo = pointService.use(command);

            // then
            verify(mockPointRepository, times(1)).save(any(Point.class));
            assertThat(pointInfo.point()).isEqualTo(currentPoint - usePoint);
        }
    }
}
