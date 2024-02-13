package com.task10.model;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.*;

@DynamoDBTable(tableName = "cmtr-52e956b4-Tables-test")
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TablesModel
{
    @DynamoDBHashKey private int id;
    @DynamoDBAttribute private int number;
    @DynamoDBAttribute private int places;
    @DynamoDBAttribute private boolean isVip;
    @DynamoDBAttribute private int minOrder;
}
