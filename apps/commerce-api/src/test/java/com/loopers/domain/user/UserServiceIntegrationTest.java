package com.loopers.domain.user;

import static org.mockito.ArgumentMatchers.any;

import com.loopers.application.user.UserCommand.UserCreateCommand;
import com.loopers.utils.DatabaseCleanUp;
import java.time.LocalDate;
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
            Mockito.verify(spyUserRepository).save(any(User.class));
        }
    }
}
