package com.loopers.domain.user;

import java.util.Optional;

public interface UserRepository {
    UserModel save(UserModel user);
    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);
    Optional<UserModel> findByUserId(String userId);
}
