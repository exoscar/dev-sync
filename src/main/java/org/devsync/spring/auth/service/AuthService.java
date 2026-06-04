package org.devsync.spring.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devsync.spring.auth.dto.RegisterRequest;
import org.devsync.spring.auth.dto.RegisterResponse;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.auth.repository.UserRepository;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public RegisterResponse registerUser(RegisterRequest request) {
        String email = request.getEmail();
        boolean isExisting = userRepository.existsByEmail(email);

        if (isExisting) {
            log.warn("Register Failed: email={} already exists", email);
            throw new BusinessException("Email already Exists", ErrorCode.USER_EMAIL_ALREADY_EXISTS);
        }
        String password = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(password);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user = userRepository.save(user);

        return RegisterResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getFirstName())
                .id(user.getId())
                .email(user.getEmail()).build();

    }

}
