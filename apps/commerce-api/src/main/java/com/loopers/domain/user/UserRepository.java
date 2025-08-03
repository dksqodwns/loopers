package com.loopers.domain.user;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByid(Long id);

    User save(User user);
}
