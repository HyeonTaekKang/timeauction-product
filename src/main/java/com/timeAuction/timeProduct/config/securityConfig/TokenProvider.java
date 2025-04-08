package com.timeAuction.timeProduct.config.securityConfig;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class TokenProvider {

    @Value("${aws.cognito-poolid}")
    private String cognitoPoolId;

    @Value("${aws.cognito-region}")
    private String region;

    private final UserDetailsService userDetailsService;

    public TokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public boolean validateToken(String token) {
        try {
            log.info("Validating token...");

            // JWT를 '.'로 분리하여 Header, Payload, Signature를 추출
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                log.error("Invalid JWT token structure");
                return false; // 유효하지 않은 경우 false 반환
            }

            // Base64Url로 인코딩된 헤더를 디코드
            String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]));
            JsonNode headerNode = new ObjectMapper().readTree(headerJson);

            // JWT 헤더에서 kid 추출
            String kid = headerNode.get("kid").asText();
            log.info("Extracted kid: {}", kid);

            // 공개 키 가져오기 및 클레임 파싱
            PublicKey publicKey = getPublicKey(kid).orElseThrow(() -> new RuntimeException("No public key found"));
            Claims claims = Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token)
                    .getBody();

            // 만료 시간 체크
            if (claims.getExpiration().before(new Date())) {
                log.warn("Token has expired");
                return false; // 만료된 경우 false 반환
            }

            return true; // 유효한 경우 true 반환
        } catch (IOException e) {
            log.error("Failed to parse JWT header: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false; // 예외 발생 시 false 반환
        }
        return false; // 예외 발생 시 false 반환
    }

    public Authentication getAuthentication(String userEmail) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private Optional<PublicKey> getPublicKey(String kid) {
        try {
            URL url = new URL("https://cognito-idp." + region + ".amazonaws.com/" + cognitoPoolId + "/.well-known/jwks.json");
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(url);

            // JWKS에서 kid에 해당하는 키 찾기
            for (JsonNode key : jsonNode.get("keys")) {
                if (key.get("kid").asText().equals(kid)) {
                    String modulus = key.get("n").asText();
                    String exponent = key.get("e").asText();
                    log.info("Found matching key for kid: {}", kid);
                    return Optional.of(getPublicKey(modulus, exponent));
                }
            }
            log.warn("No matching public key found for kid: {}", kid);
        } catch (IOException e) {
            log.error("Error fetching JWKS: {}", e.getMessage());
        }
        return Optional.empty();
    }

    private PublicKey getPublicKey(String modulus, String exponent) {
        byte[] modulusBytes = Base64.getUrlDecoder().decode(modulus);
        byte[] exponentBytes = Base64.getUrlDecoder().decode(exponent);
        RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger(1, modulusBytes), new BigInteger(1, exponentBytes));
        try {
            return KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate public key", e);
        }
    }
}
