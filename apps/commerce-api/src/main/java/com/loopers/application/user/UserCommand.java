package com.loopers.application.user;

import com.loopers.domain.user.Gender;
import java.time.LocalDate;

public class UserCommand {
    public record UserCreateCommand(
            String userId,
            String email,
            Gender gender,
            LocalDate birthDate
    ) { }
}
