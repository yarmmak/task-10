package com.task10.strategy.impl;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.task10.dto.ReservationsRequest;
import com.task10.service.DynamoDbClient;
import com.task10.strategy.ApiStrategy;

public class PersistReservationApiStrategy implements ApiStrategy
{
    private final Gson gson;
    private final DynamoDbClient dbClient;

    public PersistReservationApiStrategy(DynamoDbClient dbClient, Gson gson)
    {
        this.gson = gson;
        this.dbClient = dbClient;
    }

    @Override
    public APIGatewayProxyResponseEvent processApi(final APIGatewayProxyRequestEvent input)
    {
        ReservationsRequest reservationsRequest = gson.fromJson(input.getBody(), ReservationsRequest.class);
        APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();

        System.out.println(input.getBody());

        try
        {
            if (dbClient.getAllTables().getTables().stream().noneMatch(table -> table.getNumber() == reservationsRequest.getTableNumber()))
            {
                System.out.println("Table does not exist!");
                throw new RuntimeException();
            }

            apiGatewayProxyResponseEvent
                    .withBody(gson.toJson(dbClient.createReservation(reservationsRequest)));
        }
        catch (Exception ex)
        {
            System.out.println(ex);
            apiGatewayProxyResponseEvent.withStatusCode(400);
        }

        return apiGatewayProxyResponseEvent;
    }

    @Override
    public boolean isApplicable(final String path, final String method)
    {
        return "/reservations".equals(path) && POST_REQUEST.equals(method);
    }
}
