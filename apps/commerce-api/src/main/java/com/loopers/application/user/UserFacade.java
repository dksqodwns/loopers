package com.loopers.application.user;

import com.loopers.domain.point.PointService;
import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFacade {
    private final UserService userService;
    private final PointService pointService;

    public UserInfo createUser(UserCommand.UserCreateCommand command) {
        UserModel user = userService.createUser(command);
        return UserInfo.from(user);
    }

    public UserInfo getUser(String userId) {
        return userService.getUserByUserId(userId)
                .map(UserInfo::from)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "유저를 찾을 수 없습니다."));
    }

}
