package com.loopers.domain.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loopers.application.point.PointCommand.PointChargeCommand;
import com.loopers.application.user.UserCommand.UserCreateCommand;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserRepository;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest
public class PointServiceIntegrationTest {
    @MockitoSpyBean
    private UserRepository spyUserRepository;
    @MockitoSpyBean
    private PointRepository spyPointRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    public void cleanUp() {
        this.databaseCleanUp.truncateAllTables();
    }

    @DisplayName("포인트 충전 시,")
    @Nested
    class Charge {
        @DisplayName("존재하지 않는 유저 ID로 충전을 시도한 경우, 실패한다.")
        @Test
        void chargePoint_whenNotExistsUserId() {
            // given
            PointService pointService = new PointService(spyUserRepository, spyPointRepository);

            PointChargeCommand command = new PointChargeCommand("non-exists-user-id", 1_000);

            // when
            CoreException coreException = assertThrows(CoreException.class, () -> pointService.charge(command));

            // then
            assertThat(coreException.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }
    }


    @DisplayName("포인트 조회 시,")
    @Nested
    class GetPoint {
        @DisplayName("해당 ID의 회원이 존재하면 보유 포인트가 반환된다.")
        @Test
        void getPoint_whenExistsUser() {
            // given
            UserService userService = new UserService(spyUserRepository);
            PointService pointService = new PointService(spyUserRepository, spyPointRepository);
            UserCreateCommand command = new UserCreateCommand("test", "테스터", "test@test.com", Gender.MALE, LocalDate.of(1998, 1, 8));
            UserModel user = userService.createUser(command);

            // when
            PointModel points = pointService.getPoints(user.getUserId());

            // then
            assertThat(points).isNotNull();
        }

        @DisplayName("해당 ID의 회원이 존재하지 않으면 null이 반환된다.")
        @Test
        void getPoint_whenNotExistsUser() {
            // given
            PointService pointService = new PointService(spyUserRepository, spyPointRepository);

            // when
            PointModel points = pointService.getPoints("non-exists-user-id");

            // then
            assertThat(points).isNull();
        }
    }
}
