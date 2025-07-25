package com.loopers.infrastructure.user;

import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public UserModel save(UserModel user) {
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
    public Optional<UserModel> findByUserId(String userId) {
        return this.userJpaRepository.findByUserId(userId);
    }
}
