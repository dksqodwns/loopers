package com.loopers.domain.user;

import com.loopers.application.user.UserCommand.UserCreateCommand;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    private void validateUserCreated(UserCreateCommand command) {
        if (this.userRepository.existsByUserId(command.userId())) {
            throw new CoreException(ErrorType.CONFLICT, "이미 존재하는 아이디 입니다.");
        }

        if (this.userRepository.existsByEmail(command.email())) {
            throw new CoreException(ErrorType.CONFLICT, "이미 존재하는 이메일 입니다.");
        }
    }

    @Transactional
    public User createUser(UserCreateCommand command) {
        this.validateUserCreated(command);
        User user = User.create(command);
        return this.userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByUserId(String userId) {
        return this.userRepository.findByUserId(userId);
    }

}
