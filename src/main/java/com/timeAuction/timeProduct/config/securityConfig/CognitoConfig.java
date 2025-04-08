package com.timeAuction.timeProduct.config.securityConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
public class CognitoConfig {

    @Value("${aws.cognito-region}")
    private String awsRegion;

    @Bean
    public CognitoIdentityProviderClient cognitoIdentityProvider() {
        return CognitoIdentityProviderClient.builder()
                .region(Region.of(awsRegion))
                .build();
    }
}
