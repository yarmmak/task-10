package com.task10.strategy.impl;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.task10.service.DynamoDbClient;
import com.task10.strategy.ApiStrategy;

public class RetrieveTablesApiStrategy implements ApiStrategy
{
    private final Gson gson;
    private final DynamoDbClient dbClient;

    public RetrieveTablesApiStrategy(DynamoDbClient dbClient, Gson gson)
    {
        this.gson = gson;
        this.dbClient = dbClient;
    }

    @Override
    public APIGatewayProxyResponseEvent processApi(final APIGatewayProxyRequestEvent input)
    {
        return new APIGatewayProxyResponseEvent()
                .withBody(gson.toJson(dbClient.getAllTables()));
    }

    @Override
    public boolean isApplicable(final String path, final String method)
    {
        return "/tables".equals(path) && GET_REQUEST.equals(method);
    }
}
