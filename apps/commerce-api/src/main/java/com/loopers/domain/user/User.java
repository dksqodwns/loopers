package com.loopers.domain.user;

import com.loopers.application.user.UserCommand.UserCreateCommand;
import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    private String userId;
    private String email;
    private Gender gender;
    private LocalDate birthDate;

    public static User create(UserCreateCommand command) {
        return new User(
                command.userId(),
                command.email(),
                command.gender(),
                command.birthDate()
        );
    }

    private User(String userId, String email, Gender gender, LocalDate birthDate) {
        UserValidator.validate(userId, email, gender);
        this.userId = userId;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
    }

}
