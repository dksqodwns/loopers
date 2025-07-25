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
public class UserModel extends BaseEntity {
    private String userId;
    private String username;
    private String email;
    private Gender gender;
    private LocalDate birthDate;

    private UserModel(String userId, String username, String email, Gender gender, LocalDate birthDate) {
        UserValidator.validate(userId, email, gender);
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public static UserModel create(UserCreateCommand command) {
        return new UserModel(
                command.userId(),
                command.username(),
                command.email(),
                command.gender(),
                command.birthDate()
        );
    }

}
