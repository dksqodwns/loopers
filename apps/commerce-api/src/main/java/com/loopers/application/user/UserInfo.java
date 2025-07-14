package com.loopers.application.user;

import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;
import java.time.LocalDate;

public record UserInfo(Long id, String userId, String email, Gender gender, LocalDate birthDate) {
    public static UserInfo from(User user) {
        return new UserInfo(
                user.getId(),
                user.getUserId(),
                user.getEmail(),
                user.getGender(),
                user.getBirthDate()
        );
    }
}
