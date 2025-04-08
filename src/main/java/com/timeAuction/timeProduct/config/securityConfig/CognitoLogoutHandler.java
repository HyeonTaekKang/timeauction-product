package com.timeAuction.timeProduct.config.securityConfig;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GlobalSignOutRequest;

@Component
@RequiredArgsConstructor
@Slf4j
public class CognitoLogoutHandler implements LogoutHandler {
    private final CognitoIdentityProviderClient cognitoIdentityProviderClient;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // Authorization 헤더에서 Access Token 추출
        String authHeader = request.getHeader("Authorization");
        String accessToken = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            accessToken = authHeader.substring(7); // "Bearer " 제거
        }

        if (accessToken != null) {
            try {
                // Cognito에서 전역 로그아웃 요청
                cognitoIdentityProviderClient.globalSignOut(GlobalSignOutRequest.builder()
                        .accessToken(accessToken)
                        .build());

                // Refresh Token 쿠키 삭제
                deleteCookie(response, "REFRESH_TOKEN");
                log.info("User logged out successfully.");
            } catch (Exception e) {
                log.error("Logout failed: {}", e.getMessage());
                // 로그아웃 실패 처리 (예: 사용자에게 알림)
            }
        } else {
            log.warn("Access token not found in the request.");
        }
    }

    private void deleteCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
