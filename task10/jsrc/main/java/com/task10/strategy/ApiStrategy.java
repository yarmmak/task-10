package com.task10.strategy;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public interface ApiStrategy
{
    String GET_REQUEST = "GET";
    String POST_REQUEST = "POST";

    APIGatewayProxyResponseEvent processApi(APIGatewayProxyRequestEvent input);
    boolean isApplicable(String path, String method);
}
