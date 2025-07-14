package com.loopers.domain.user;

import com.loopers.application.user.UserCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User createUser(UserCommand.UserCreateCommand command) {
        return this.userRepository.save(User.create(command));
    }
}
