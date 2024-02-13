package com.task10.strategy.impl;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.task10.dto.TablesRequest;
import com.task10.service.DynamoDbClient;
import com.task10.strategy.ApiStrategy;

public class PersistTableStrategy implements ApiStrategy
{
    private final Gson gson;
    private final DynamoDbClient dbClient;

    public PersistTableStrategy(DynamoDbClient dbClient, Gson gson)
    {
        this.gson = gson;
        this.dbClient = dbClient;
    }

    @Override
    public APIGatewayProxyResponseEvent processApi(final APIGatewayProxyRequestEvent input)
    {
        TablesRequest tablesRequest = gson.fromJson(input.getBody(), TablesRequest.class);

        return new APIGatewayProxyResponseEvent()
                .withBody(gson.toJson(dbClient.createTable(tablesRequest)));
    }

    @Override
    public boolean isApplicable(final String path, final String method)
    {
        return "/tables".equals(path) && POST_REQUEST.equals(method);
    }
}
