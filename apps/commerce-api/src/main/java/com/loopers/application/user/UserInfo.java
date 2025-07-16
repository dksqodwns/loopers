package com.loopers.application.user;

import com.loopers.domain.user.Gender;
import com.loopers.domain.user.UserModel;
import java.time.LocalDate;

public record UserInfo(Long id, String userId, String username, String email, Gender gender, LocalDate birthDate) {
    public static UserInfo from(UserModel user) {
        return new UserInfo(
                user.getId(),
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getGender(),
                user.getBirthDate()
        );
    }
}
