package com.loopers.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import com.loopers.application.user.UserCommand.UserCreateCommand;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest
public class UserServiceIntegrationTest {
    @MockitoSpyBean
    private UserRepository spyUserRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    String userId;
    String username;
    String email;
    Gender gender;
    LocalDate birthDate;

    @BeforeEach
    void init() {
        userId = "test";
        username = "테스터";
        email = "test@test.com";
        gender = Gender.MALE;
        birthDate = LocalDate.now().minusDays(1);
    }

    @AfterEach
    public void cleanUp() {
        this.databaseCleanUp.truncateAllTables();
    }

    @DisplayName("회원가입 시,")
    @Nested
    class Post {
        @DisplayName("유저 저장이 수행된다.")
        @Test
        void createUser_whenRegisterUser() {
            UserService userService = new UserService(spyUserRepository);
            UserCreateCommand command = new UserCreateCommand(
                    userId, username, email, gender, birthDate
            );
            userService.createUser(command);
            Mockito.verify(spyUserRepository).save(any(UserModel.class));
        }

        @DisplayName("중복된 유저 ID로 가입 시, 저장이 실패한다.")
        @Test
        void fail_whenRegisterExistsUserId() {
            UserService userService = new UserService(spyUserRepository);
            UserCreateCommand command = new UserCreateCommand(
                    userId, username, email, gender, birthDate
            );
            userService.createUser(command);

            String existsUserId = "test";
            UserCreateCommand existsCommand = new UserCreateCommand(
                    existsUserId, username, email, gender, birthDate
            );
            // 호출에 대한 ...
            CoreException userIdConflictException = assertThrows(CoreException.class,
                    () -> userService.createUser(existsCommand));
            assertThat(userIdConflictException.getErrorType()).isEqualTo(ErrorType.CONFLICT);
        }
    }

    @DisplayName("유저 정보 조회 시")
    @Nested
    class Get {
        @DisplayName("해당 ID의 회원이 존재 할 경우, 회원 정보가 반환 된다.")
        @Test
        void getUser_whenUserExists() {
            UserService userService = new UserService(spyUserRepository);
            UserCreateCommand command = new UserCreateCommand(
                    userId, username, email, gender, birthDate
            );
            UserModel savedUser = userService.createUser(command);

            Optional<UserModel> findUser = userService.getUserByUserId(userId);

            assertAll(
                    () -> assertThat(findUser).isNotNull(),
                    () -> assertThat(userId).isEqualTo(findUser.get().getUserId()),
                    () -> assertThat(savedUser.getUserId()).isEqualTo(findUser.get().getUserId())
            );
        }

        @DisplayName("해당 ID의 회원이 존재하지 않을 경우, null이 반환 된다.")
        @Test
        void getNull_whenUserNotExists() {
            UserService userService = new UserService(spyUserRepository);
            Optional<UserModel> findUser = userService.getUserByUserId("non-exists-user-id");

            assertThat(findUser).isEmpty();
        }
    }
}
