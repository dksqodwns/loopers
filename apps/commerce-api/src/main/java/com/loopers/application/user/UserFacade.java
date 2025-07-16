package com.loopers.application.user;

import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFacade {
    private final UserService userService;

    public UserInfo createUser(UserCommand.UserCreateCommand command) {
        UserModel user = userService.createUser(command);
        return UserInfo.from(user);
    }

    public UserInfo getUser(String userId) {
        Optional<UserModel> user = userService.getUserByUserId(userId);

        if (user.isPresent()) {
            return UserInfo.from(user.get());
        } else {
            throw new CoreException(ErrorType.NOT_FOUND, "유저를 찾을 수 없습니다.");
        }
    }
}
