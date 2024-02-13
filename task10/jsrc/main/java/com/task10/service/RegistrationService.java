package com.task10.service;

import com.task10.dto.SignInRequestData;
import com.task10.dto.SignUpRequestData;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.HashMap;
import java.util.Map;

public class RegistrationService {
    private CognitoIdentityProviderClient cognitoClient;

    public RegistrationService(CognitoIdentityProviderClient cognitoClient)
    {
        this.cognitoClient = cognitoClient;
    }

    public void signUpAccount(final SignUpRequestData requestData)
    {
        String name = requestData.getFirstName();
        String email = requestData.getEmail();
        String password = requestData.getPassword();

        registerUser(name, retrieveUserPoolClientsId(retrieveUserPoolId()), email, password);
        System.out.println("User with email " + email + " is registered!");
        confirmRegistrationByAdmin(email);
    }

    public AdminInitiateAuthResponse signInAccount(final SignInRequestData requestData)
    {
        String username = requestData.getEmail();
        String password = requestData.getPassword();
        String userPoolId = retrieveUserPoolId();

        Map<String, String> authParameters = new HashMap<>();
        authParameters.put("USERNAME", username);
        authParameters.put("PASSWORD", password);

        AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
                .clientId(retrieveUserPoolClientsId(userPoolId))
                .userPoolId(userPoolId)
                .authParameters(authParameters)
                .authFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                .build();

        return cognitoClient.adminInitiateAuth(authRequest);
    }

    private void registerUser(final String name, final String clientId, final String email, final String password)
    {

        AttributeType userAttributes = AttributeType.builder()
                .name("name").value(name)
                .name("email").value(email)
                .build();

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .clientId(clientId)
                .userAttributes(userAttributes)
                .username(email)
                .password(password)
                .build();

        cognitoClient.signUp(signUpRequest);
    }

    private String retrieveUserPoolId()
    {
        return cognitoClient.listUserPools(ListUserPoolsRequest.builder().build())
                .userPools().stream().findFirst().get().id();
    }

    private String retrieveUserPoolClientsId(final String userPoolId)
    {
        return cognitoClient.listUserPoolClients(ListUserPoolClientsRequest.builder().userPoolId(userPoolId).build())
                .userPoolClients().stream().findFirst().get().clientId();
    }

    private void confirmRegistrationByAdmin(final String userEmail) {
        AdminConfirmSignUpRequest confirmationRequest = AdminConfirmSignUpRequest.builder()
                .userPoolId(retrieveUserPoolId())
                .username(userEmail)
                .build();

        System.out.println(retrieveUserPoolId());
        System.out.println(userEmail);
        System.out.println(confirmationRequest.toString());
        cognitoClient.adminConfirmSignUp(confirmationRequest);
    }
}

