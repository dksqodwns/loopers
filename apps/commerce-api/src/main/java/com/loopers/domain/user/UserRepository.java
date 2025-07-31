package com.loopers.domain.user;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);
    Optional<User> findByUserId(String userId);
}
