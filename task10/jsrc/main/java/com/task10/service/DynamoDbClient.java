package com.task10.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.*;
import com.task10.dto.*;
import com.task10.model.ReservationsModel;
import com.task10.model.TablesModel;

import java.util.*;

public class DynamoDbClient {

    private AmazonDynamoDB amazonDynamoDB;

    public DynamoDbClient(AmazonDynamoDB amazonDynamoDB)
    {
        this.amazonDynamoDB = amazonDynamoDB;
    }

    public TablesResponse getAllTables() {
        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
        PaginatedScanList<TablesModel> tables = mapper.scan(TablesModel.class, new DynamoDBScanExpression());

        tables.loadAllResults();

        return TablesResponse.builder()
                .tables(tables)
                .build();
    }

    public TableCreationResponse createTable(final TablesRequest tablesRequest) {
        DynamoDBMapper dbMapper = new DynamoDBMapper(amazonDynamoDB);

        TablesModel tableToCreate = new TablesModel();
        tableToCreate.setId(tablesRequest.getId());
        tableToCreate.setNumber(tablesRequest.getNumber());
        tableToCreate.setPlaces(tablesRequest.getPlaces());
        tableToCreate.setVip(tablesRequest.isVip());
        tableToCreate.setMinOrder(tablesRequest.getMinOrder());

        dbMapper.save(tableToCreate);

        return TableCreationResponse.builder()
                .id(tableToCreate.getId())
                .build();
    }

    public TablesModel getTableById(final int tableId) {
        DynamoDBMapper dbMapper = new DynamoDBMapper(amazonDynamoDB);

        return dbMapper.load(TablesModel.class, tableId);
    }

    public ReservationsResponse getAllReservations() {
        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
        PaginatedScanList<ReservationsModel> reservations = mapper.scan(ReservationsModel.class, new DynamoDBScanExpression());

        reservations.loadAllResults();

        return ReservationsResponse.builder()
                .reservations(reservations)
                .build();
    }

    public ReservationCreationResponse createReservation(final ReservationsRequest reservationsRequest) {
        DynamoDBMapper dbMapper = new DynamoDBMapper(amazonDynamoDB);
        ScanRequest scanRequest = new ScanRequest().withTableName("cmtr-52e956b4-Reservations-test")
                .withScanFilter(parseConditions(reservationsRequest));
        ScanResult result = amazonDynamoDB.scan(scanRequest);

        if (result.getCount() > 0)
        {
            throw new RuntimeException("Reservation already exists in Database!");
        }

        ReservationsModel reservationsModel = buildReservationsModel(reservationsRequest);
        dbMapper.save(reservationsModel);

        return ReservationCreationResponse.builder()
                .reservationId(reservationsModel.getId())
                .build();
    }

    private ReservationsModel buildReservationsModel(final ReservationsRequest reservationsRequest)
    {
        return ReservationsModel.builder()
                .id(UUID.randomUUID().toString())
                .tableNumber(reservationsRequest.getTableNumber())
                .clientName(reservationsRequest.getClientName())
                .phoneNumber(reservationsRequest.getPhoneNumber())
                .date(reservationsRequest.getDate())
                .slotTimeStart(reservationsRequest.getSlotTimeStart())
                .slotTimeEnd(reservationsRequest.getSlotTimeEnd())
                .build();
    }

    private Map<String, Condition> parseConditions(final ReservationsRequest reservationsRequest)
    {
        Condition clientNameCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue().withS(reservationsRequest.getClientName()));
        Condition phoneNumberCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue().withS(reservationsRequest.getPhoneNumber()));
        Condition dateCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue().withS(reservationsRequest.getDate()));
        Condition slotTimeStartCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue().withS(reservationsRequest.getSlotTimeStart()));
        Condition slotTimeEndCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue().withS(reservationsRequest.getSlotTimeEnd()));

        Map<String, Condition> conditions = new HashMap<>();
        conditions.put("clientName", clientNameCondition);
        conditions.put("phoneNumber", phoneNumberCondition);
        conditions.put("date", dateCondition);
        conditions.put("slotTimeStart", slotTimeStartCondition);
        conditions.put("slotTimeEnd", slotTimeEndCondition);

        return conditions;
    }
}
