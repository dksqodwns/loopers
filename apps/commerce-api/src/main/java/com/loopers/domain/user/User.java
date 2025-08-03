package com.loopers.domain.user;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity {
    private String loginId;
    private String email;
    private String username;
    private LocalDate birthDate;
    private Gender gender;

    public User(String loginId, String email, String username, String birthDate, String gender) {
        UserValidator.validate(loginId, email, gender);
        this.loginId = loginId;
        this.email = email;
        this.username = username;
        this.birthDate = LocalDate.parse(birthDate);
        this.gender = Gender.valueOf(gender);
    }
}



