package com.task10.strategy.impl;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.task10.dto.SignInRequestData;
import com.task10.dto.SignInResponse;
import com.task10.service.RegistrationService;
import com.task10.strategy.ApiStrategy;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;

public class SignInApiStrategy implements ApiStrategy {

    private RegistrationService registrationService;
    private Gson gson;

    public SignInApiStrategy(RegistrationService registrationService, Gson gson)
    {
        this.registrationService = registrationService;
        this.gson = gson;
    }

    @Override
    public APIGatewayProxyResponseEvent processApi(APIGatewayProxyRequestEvent input)
    {
        SignInRequestData requestData = gson.fromJson(input.getBody(), SignInRequestData.class);
        APIGatewayProxyResponseEvent responseEvent;
        AdminInitiateAuthResponse adminInitiateAuthResponse;
        SignInResponse response;
        
        try
        {
            adminInitiateAuthResponse = registrationService.signInAccount(requestData);
            response = SignInResponse.builder()
                    .accessToken(adminInitiateAuthResponse.authenticationResult().accessToken())
                    .build();
            responseEvent = new APIGatewayProxyResponseEvent().withBody(gson.toJson(response));
        }
        catch (Exception ex)
        {
            System.out.println(ex);
            responseEvent = new APIGatewayProxyResponseEvent().withStatusCode(400);
        }

        return responseEvent;
    }

    @Override
    public boolean isApplicable(String path, String method)
    {
        return "/signin".equals(path) && POST_REQUEST.equals(method);
    }
}
