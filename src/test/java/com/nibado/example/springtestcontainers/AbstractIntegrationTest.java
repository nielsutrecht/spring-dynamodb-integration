package com.nibado.example.springtestcontainers;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.containers.GenericContainer;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {
    private static final int DYNAMO_PORT = 8000;
    @ClassRule
    public static GenericContainer dynamoDb =
        new GenericContainer("amazon/dynamodb-local:1.11.119")
            .withExposedPorts(DYNAMO_PORT);

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            String endpoint = String.format("aws.dynamo.endpoint=http://%s:%s",
                dynamoDb.getContainerIpAddress(),
                dynamoDb.getMappedPort(DYNAMO_PORT));

            TestPropertyValues.of(endpoint).applyTo(configurableApplicationContext);
        }
    }
}
