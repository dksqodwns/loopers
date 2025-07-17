package com.loopers.interfaces.api.user;

import static com.loopers.interfaces.api.user.UserDto.UserCreateRequest;
import static com.loopers.interfaces.api.user.UserDto.UserResponse;

import com.loopers.application.point.PointFacade;
import com.loopers.application.point.PointInfo;
import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserInfo;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.point.PointDto.PointResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserFacade userFacade;
    private final PointFacade pointFacade;

    @PostMapping("")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request) {
        UserInfo userInfo = this.userFacade.createUser(request.toCommand());
        UserResponse response = UserResponse.from(userInfo);

        return ApiResponse.success(response);
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getUser(HttpServletRequest request) {
        String userId = request.getHeader("X-USER-ID");

        UserInfo userInfo = this.userFacade.getUser(userId);
        UserResponse response = UserResponse.from(userInfo);
        return ApiResponse.success(response);
    }

    @PostMapping("/points")
    public ApiResponse<PointResponse> charge(HttpServletRequest httpServletRequest, @RequestBody @Valid int point) {
        String userId = httpServletRequest.getHeader("X-USER-ID");
        PointInfo pointInfo = this.pointFacade.chargePoint(userId, point);
        PointResponse response = PointResponse.from(pointInfo);
        return ApiResponse.success(response);
    }

    @GetMapping("/points")
    public ApiResponse<PointResponse> getPoints(HttpServletRequest request) {
        String userId = request.getHeader("X-USER-ID");
        PointInfo pointInfo = this.pointFacade.getPoints(userId);
        PointResponse response = PointResponse.from(pointInfo);

        return ApiResponse.success(response);
    }
}
