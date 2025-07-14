package com.loopers.application.user;

import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFacade {
    private final UserService userService;

    public UserInfo createUser(UserCommand.UserCreateCommand command) {
        User user = userService.createUser(command);
        return UserInfo.from(user);
    }
}
