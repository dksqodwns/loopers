package com.loopers.interfaces.api.user;

import static com.loopers.interfaces.api.user.UserDto.UserCreateRequest;
import static com.loopers.interfaces.api.user.UserDto.UserResponse;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserInfo;
import com.loopers.interfaces.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserFacade userFacade;

    @PostMapping("")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request) {
        UserInfo userInfo = this.userFacade.createUser(request.toCommand());
        UserResponse response = UserResponse.from(userInfo);

        return ApiResponse.success(response);
    }
}
