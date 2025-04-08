package com.timeAuction.timeProduct.config.securityConfig;


import com.timeAuction.timeProduct.entity.user.User;
import com.timeAuction.timeProduct.exception.InvalidUsernameOrPassword;
import com.timeAuction.timeProduct.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.timeAuction.timeProduct.entity.user.UserRole.USER;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidUsernameOrPassword());

        if (user.getUserRole() == USER) {
            return new UserPrincipal(user);
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }
    }
}