package com.loopers.domain.user;

import com.loopers.application.user.UserCommand.UserCreateCommand;
import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

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
        this.validate(userId, email, birthDate);
        this.userId = userId;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    private void validate(
            String userId, String email, LocalDate birthDate
    ) {
        Assert.hasText(userId, "아이디는 비어있을 수 없습니다.");
        Assert.hasText(email, "이메일은 비어있을 수 없습니다.");
        Assert.notNull(birthDate, "생년월일은 비어있을 수 없습니다.");
        Assert.isTrue(email.contains("@"), "유효하지 않은 이메일 형식 입니다.");
        Assert.isTrue(birthDate.isBefore(LocalDate.now()), "생년월일은 현재보다 이전이어야 합니다.");
    }
}
