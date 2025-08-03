package com.loopers.interfaces.api.user;

public record UserDto() {

    public record V1() {

        public record RegisterRequest(
                String loginId,
                String email,
                String username,
                String birthDate,
                String gender
        ) {

        }

        public record UserResponse(
                Long id,
                String loginId,
                String email,
                String username,
                String birthDate,
                String gender
        ) {

        }
    }
}
