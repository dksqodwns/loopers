package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.springframework.util.StringUtils;

public class UserValidator {
    private static final String USER_ID_REGEX = "^[a-zA-Z0-9]{1,10}$";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    public static void validate(String loginId, String email, String gender) {
        if (!StringUtils.hasText(loginId)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "아이디는 비어있을 수 없습니다.");
        }

        if (!loginId.matches(USER_ID_REGEX)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "아이디는 10자를 넘을 수 없습니다.");
        }

        if (!StringUtils.hasText(email)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "이메일은 비어있을 수 없습니다.");
        }

        if (!email.matches(EMAIL_REGEX)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "유효하지 않은 이메일 형식입니다.");
        }

        if (gender == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "성별은 비어있을 수 없습니다.");
        }
    }
}
