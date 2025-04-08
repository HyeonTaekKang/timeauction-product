package com.timeAuction.timeProduct.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final CognitoIdentityProviderClient cognitoIdentityProviderClient;

    @Transactional
    public String getUserEmail(String authHeader) {

        String accessToken = authHeader.replace("Bearer ", "");

        GetUserRequest getUserRequest = GetUserRequest.builder()
                .accessToken(accessToken)
                .build();
        GetUserResponse getUserResponse = cognitoIdentityProviderClient.getUser(getUserRequest);

        List<AttributeType> userAttributes = getUserResponse.userAttributes();
        return userAttributes.stream()
                .filter(attr -> attr.name().equals("email"))
                .findFirst()
                .map(AttributeType::value)
                .orElse(null);
    }

}
