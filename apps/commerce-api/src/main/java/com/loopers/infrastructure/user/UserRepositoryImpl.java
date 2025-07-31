package com.loopers.infrastructure.user;

import com.loopers.domain.user.User;
import com.loopers.domain.user.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        return this.userJpaRepository.save(user);
    }

    @Override
    public boolean existsByUserId(String userId) {
        return this.userJpaRepository.existsByUserId(userId);
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.userJpaRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return this.userJpaRepository.findByUserId(userId);
    }
}
