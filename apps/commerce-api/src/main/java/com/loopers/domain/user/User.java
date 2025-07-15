package com.loopers.domain.user;

import com.loopers.application.user.UserCommand.UserCreateCommand;
import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    private String userId;
    private String username;
    private String email;
    private Gender gender;
    private LocalDate birthDate;

    private User(String userId, String username, String email, Gender gender, LocalDate birthDate) {
        UserValidator.validate(userId, email, gender);
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public static User create(UserCreateCommand command) {
        return new User(
                command.userId(),
                command.username(),
                command.email(),
                command.gender(),
                command.birthDate()
        );
    }

}
