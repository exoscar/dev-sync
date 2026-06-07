package org.devsync.spring.common.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devsync.spring.auth.entity.User;
import org.devsync.spring.auth.repository.UserRepository;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BusinessException(
                                "User not found",
                                ErrorCode.NOT_FOUND
                        ));

        return mapToUserDetails(user);

    }

    public CustomUserDetails loadUserById(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new BusinessException(
                                "User not found",
                                ErrorCode.NOT_FOUND
                        ));

        return mapToUserDetails(user);
    }

    private CustomUserDetails mapToUserDetails(User user) {
        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                List.of(
                        new SimpleGrantedAuthority("ROLE_"+user.getRole().getRole())
                )
        );
    }
}
