package com.task10.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.*;

@DynamoDBTable(tableName = "cmtr-52e956b4-Reservations-test")
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationsModel
{
    @DynamoDBHashKey private String id;
    @DynamoDBAttribute private int tableNumber;
    @DynamoDBAttribute private String clientName;
    @DynamoDBAttribute private String phoneNumber;
    @DynamoDBAttribute private String date;
    @DynamoDBAttribute private String slotTimeStart;
    @DynamoDBAttribute private String slotTimeEnd;
}
