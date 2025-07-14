package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserCommand.UserCreateCommand;
import com.loopers.application.user.UserInfo;
import com.loopers.domain.user.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public class UserDto {
    public record UserResponse(Long id, String userId, String email) {
        public static UserResponse from(UserInfo userInfo) {
            return new UserResponse(
                    userInfo.id(),
                    userInfo.userId(),
                    userInfo.email()
            );
        }
    }

    public record UserCreateRequest(
            @NotEmpty(message = "ID는 비어있을 수 없습니다.")
            String userId,

            @NotEmpty(message = "이메일은 비어있을 수 없습니다.")
            @Email(message = "유효한 이메일 형식이 아닙니다.")
            String email,

            @NotEmpty(message = "성별은 비어있을 수 없습니다.")
            Gender gender,

            @NotEmpty(message = "생년월일은 비어있을 수 없습니다.")
            @Past(message = "생년월일은 현재보다 이전이어야 합니다.")
            LocalDate birthDate
    ) {
        public UserCreateCommand toCommand() {
            return new UserCreateCommand(
                    this.userId,
                    this.email,
                    this.gender,
                    this.birthDate
            );
        }
    }
}
