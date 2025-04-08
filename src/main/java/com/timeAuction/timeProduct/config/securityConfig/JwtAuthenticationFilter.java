package com.timeAuction.timeProduct.config.securityConfig;


import com.timeAuction.timeProduct.service.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();
        log.info("requestURI: {}", requestURI);
        log.info("Authorization header: {}", authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7); // "Bearer " 제거

            // 토큰 유효성 검증
            if (tokenProvider.validateToken(accessToken)) {
                String userEmail = userService.getUserEmail(authHeader);
                Authentication auth = tokenProvider.getAuthentication(userEmail);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                log.warn("Invalid token");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
        } else {
            log.warn("Invalid or missing token");
        }

        filterChain.doFilter(request, response);
    }

//    private String resolveToken(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7); // "Bearer " 제거
//        }
//        return null;
//    }
}
