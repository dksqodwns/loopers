package com.loopers.interfaces.api.user;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.user.UserDto.V1.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController implements UserV1ApiSpec {

    @Override
    @PostMapping("")
    public ApiResponse<UserDto.V1.UserResponse> regitser(@RequestBody() final UserDto.V1.RegisterRequest request) {
        return ApiResponse.success(new UserResponse(1L, "test", "test@test.com", "안병준", "1998-01-08", "m"));
    }

    @Override
    @GetMapping("/me")
    public ApiResponse<UserDto.V1.UserResponse> getMyInfo(@RequestHeader("X-USER-ID") final Long userId) {
        return ApiResponse.success(new UserResponse(1L, "test", "test@test.com", "안병준", "1998-01-08", "m"));
    }

}
