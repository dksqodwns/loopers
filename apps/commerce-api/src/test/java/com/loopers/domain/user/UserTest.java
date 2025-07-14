package com.loopers.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loopers.application.user.UserCommand.UserCreateCommand;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UserTest {

    @DisplayName("유저 모델을 생성 할 때,")
    @Nested
    class Create {
        String userId;
        String email;
        Gender gender;
        LocalDate birthDate;

        @BeforeEach
        void init() {
            userId = "test";
            email = "test@test.com";
            gender = Gender.MALE;
            birthDate = LocalDate.now().minusDays(1);
        }

        @DisplayName("모든 값이 주어지면 정상적인 유저가 생성된다.")
        @Test
        void createUser() {
            UserCreateCommand command = new UserCreateCommand(
                    userId, email, gender, birthDate
            );
            User user = User.create(command);

            assertAll(
                    () -> assertThat(user.getId()).isNotNull(),
                    () -> assertThat(user.getUserId()).isEqualTo(userId),
                    () -> assertThat(user.getEmail()).isEqualTo(email),
                    () -> assertThat(user.getGender()).isEqualTo(gender),
                    () -> assertThat(user.getBirthDate()).isNotNull(),
                    () -> assertThat(user.getBirthDate()).isInThePast()
            );
        }


        @DisplayName("유저의 ID가 비어있으면, User 객체 생성에 실패한다.")
        @Test
        void createUserWithEmptyId() {
            String empty = " ";
            UserCreateCommand command = new UserCreateCommand(
                    empty, email, gender, birthDate
            );
            CoreException coreException = assertThrows(CoreException.class, () -> User.create(command));
            assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("유저의 ID가 영문 및 숫자 10자 이내 형식에 맞지 않으면, User 객체 생성에 실패한다.")
        @Test
        void createUserWithInvalidId() {
            String invalidUserId = "something-wrong-user-id";
            UserCreateCommand command = new UserCreateCommand(
                    invalidUserId, email, gender, birthDate
            );
            CoreException coreException = assertThrows(CoreException.class, () -> User.create(command));
            assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("이메일이 형식에 맞지 않으면, User 객체 생성에 실패한다.")
        @Test
        void createUserWithInvalidEmail() {
            String invalidEmail = "something-wrong-email";
            UserCreateCommand command = new UserCreateCommand(
                    userId, invalidEmail, gender, birthDate
            );

            CoreException coreException = assertThrows(CoreException.class, () -> User.create(command));
            assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("생년월일 형식이 yyyy-MM-dd가 아니면, User 객체 생성에 실패한다.")
        @Test
        void createUserWithInvalidBirthDate() {
            String invalidBirthDateString = "01/08/1998";
            DateTimeFormatter invalidBirthDateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            LocalDate invalidBirthDate = LocalDate.parse(invalidBirthDateString, invalidBirthDateFormatter);
            System.out.println("invalidBirthDate = " + invalidBirthDate);
            UserCreateCommand command = new UserCreateCommand(
                    userId, email, gender, invalidBirthDate
            );
            CoreException coreException = assertThrows(CoreException.class, () -> User.create(command));
            assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }
}
