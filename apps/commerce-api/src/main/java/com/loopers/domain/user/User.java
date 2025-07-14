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
        this.validate(userId, email, birthDate);
        this.userId = userId;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    private boolean isLocalDateFormat(LocalDate date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(String.valueOf(date));
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void validate(
            String userId, String email, LocalDate birthDate
    ) {
        if (!StringUtils.hasText(userId)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "아이디는 비어있을 수 없습니다.");
        }
        if (!userId.matches("^[a-zA-Z0-9]{1,10}$")) {
            throw new CoreException(ErrorType.BAD_REQUEST, "아이디는 10자를 넘을 수 없습니다.");
        }

        if (!StringUtils.hasText(email)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "이메일은 비어있을 수 없습니다.");
        }
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new CoreException(ErrorType.BAD_REQUEST, "유효하지 않은 이메일 형식입니다.");
        }

        if (birthDate == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 비어있을 수 없습니다.");
        }
        if (!birthDate.isBefore(LocalDate.now())) {
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 현재보다 이전이어야 합니다.");
        }

        if (!(this.isLocalDateFormat(birthDate))) {
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 yyyy-MM-dd 형식이어야 합니다.");
        }
    }
}
