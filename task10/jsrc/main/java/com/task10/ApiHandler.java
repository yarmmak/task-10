package com.task10;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.task10.service.DynamoDbClient;
import com.task10.service.RegistrationService;
import com.task10.strategy.ApiStrategy;
import com.task10.strategy.impl.*;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

import java.util.*;

import static software.amazon.awssdk.regions.Region.EU_CENTRAL_1;

@LambdaHandler(lambdaName = "api_handler",
        roleName = "api_handler-role"
)
public class ApiHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final String AWS_REGION = "eu-central-1";
    private final List<ApiStrategy> apiStrategies;

    public ApiHandler() {
        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withRegion(AWS_REGION)
                .build();
        CognitoIdentityProviderClient cognitoIdentityProviderClient =
                CognitoIdentityProviderClient.builder().region(EU_CENTRAL_1).build();
        DynamoDbClient dynamoDbClient = new DynamoDbClient(amazonDynamoDB);
        RegistrationService registrationService = new RegistrationService(cognitoIdentityProviderClient);
        Gson gson = new Gson();

        apiStrategies = new ArrayList<>();
        apiStrategies.add(new PersistTableStrategy(dynamoDbClient, gson));
        apiStrategies.add(new RetrieveTableApiStrategy(dynamoDbClient, gson));
        apiStrategies.add(new RetrieveTablesApiStrategy(dynamoDbClient, gson));
        apiStrategies.add(new PersistReservationApiStrategy(dynamoDbClient, gson));
        apiStrategies.add(new RetrieveReservationsApiStrategy(dynamoDbClient, gson));
        apiStrategies.add(new SignUpApiStrategy(registrationService, gson));
        apiStrategies.add(new SignInApiStrategy(registrationService, gson));
    }

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log(request.toString());

        Optional<ApiStrategy> apiStrategy = apiStrategies.stream()
                .filter(strategy -> strategy.isApplicable(request.getResource(), request.getHttpMethod()))
                .findAny();

        if (apiStrategy.isPresent())
        {
            return apiStrategy.get().processApi(request);
        }
        else
        {
            return new APIGatewayProxyResponseEvent().withStatusCode(400);
        }
    }
}
