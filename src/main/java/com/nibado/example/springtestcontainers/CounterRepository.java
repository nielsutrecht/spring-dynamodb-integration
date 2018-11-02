package com.nibado.example.springtestcontainers;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Repository
public class CounterRepository {
    private static final String TABLE = "counters";

    private static final Logger log = LoggerFactory.getLogger(CounterRepository.class);

    private final AmazonDynamoDB db;
    private final DynamoDBMapper mapper;

    public CounterRepository(AmazonDynamoDB db) {
        this.db = db;
        this.mapper = new DynamoDBMapper(db);
    }

    public List<CounterEntity> findAll() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        return mapper.scanPage(CounterEntity.class, scanExpression).getResults();
    }

    public void put(String counter, int value) {
        mapper.save(new CounterEntity(counter, value));
    }

    public Optional<CounterEntity> get(String counter) {
        var entity = mapper.load(CounterEntity.class, counter);

        return Optional.ofNullable(entity);
    }

    public void delete(String counter) {
        mapper.delete(new CounterEntity(counter, 0));
    }

    @PostConstruct
    public void init() {
        log.info("Creating table '{}'", TABLE);

        CreateTableRequest request = new CreateTableRequest()
            .withAttributeDefinitions(
                new AttributeDefinition("counter", ScalarAttributeType.S))
            .withKeySchema(
                    new KeySchemaElement("counter", KeyType.HASH))
            .withProvisionedThroughput(new ProvisionedThroughput(10L, 10L))
            .withTableName("counters");

        try {
            CreateTableResult result = db.createTable(request);
        } catch (AmazonServiceException e) {
            log.error("Error creating table '{}'", TABLE, e);
            throw new RuntimeException(e);
        }
    }

    @DynamoDBTable(tableName=TABLE)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CounterEntity {
        @DynamoDBHashKey(attributeName="counter")
        private String counter;

        @DynamoDBAttribute(attributeName="value")
        private Integer value;
    }
}
