package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserCommand.UserCreateCommand;
import com.loopers.application.user.UserInfo;
import com.loopers.domain.user.Gender;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class UserDto {
    public record UserResponse(Long id, String userId, String username, String email, Gender gender, LocalDate birthDate) {
        public static UserResponse from(UserInfo userInfo) {
            return new UserResponse(
                    userInfo.id(),
                    userInfo.userId(),
                    userInfo.username(),
                    userInfo.email(),
                    userInfo.gender(),
                    userInfo.birthDate()
            );
        }
    }


    public record UserCreateRequest(
            @NotNull(message = "ID는 비어있을 수 없습니다.")
            String userId,

            @NotNull(message = "유저 이름은 비어있을 수 없습니다.")
            String username,

            @NotNull(message = "이메일은 비어있을 수 없습니다.")
            @Email(message = "유효한 이메일 형식이 아닙니다.")
            String email,

            @NotNull(message = "성별은 비어있을 수 없습니다.")
            String gender,

            @NotNull(message = "생년월일은 비어있을 수 없습니다.")
            String birthDate
    ) {
        public UserCreateCommand toCommand() {
            LocalDate parsedBirthDate;
            Gender parsedGender;
            try {
                parsedBirthDate = LocalDate.parse(this.birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 yyyy-MM-dd 형식이어야 합니다." + e.getMessage());
            }

            try {
                parsedGender = Gender.valueOf(this.gender);
            } catch (NullPointerException e) {
                throw new CoreException(ErrorType.BAD_REQUEST, "성별은 비어있을 수 없습니다." + e.getMessage());
            } catch (IllegalArgumentException e) {
                throw new CoreException(ErrorType.BAD_REQUEST, "성별은 MALE, FEMALE 중 하나입니다." + e.getMessage());
            }

            return new UserCreateCommand(
                    this.userId,
                    this.username,
                    this.email,
                    parsedGender,
                    parsedBirthDate
            );
        }
    }
}
