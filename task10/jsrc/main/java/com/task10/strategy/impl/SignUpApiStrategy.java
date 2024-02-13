package com.task10.strategy.impl;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.task10.dto.SignUpRequestData;
import com.task10.service.RegistrationService;
import com.task10.strategy.ApiStrategy;

public class SignUpApiStrategy implements ApiStrategy {

    private RegistrationService registrationService;
    private Gson gson;

    public SignUpApiStrategy(RegistrationService registrationService, Gson gson)
    {
        this.registrationService = registrationService;
        this.gson = gson;
    }

    @Override
    public APIGatewayProxyResponseEvent processApi(APIGatewayProxyRequestEvent input)
    {
        SignUpRequestData requestData = gson.fromJson(input.getBody(), SignUpRequestData.class);

        try {
            registrationService.signUpAccount(requestData);
        }
        catch (Exception ex)
        {
            System.out.println(ex);
            return new APIGatewayProxyResponseEvent().withStatusCode(400);
        }

        return new APIGatewayProxyResponseEvent().withStatusCode(200);
    }

    @Override
    public boolean isApplicable(final String path, final String method)
    {
        return "/signup".equals(path) && POST_REQUEST.equals(method);
    }
}
