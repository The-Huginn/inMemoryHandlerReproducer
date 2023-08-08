package com.thehuginn;

import io.quarkus.test.InMemoryLogHandler;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import org.jboss.logmanager.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.logging.LogManager;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestProfile(GreetingResourceTest.TestLoggerLevelProfile.class)
public class GreetingResourceTest {

    private static final java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("com.thehuginn");
    private static final InMemoryLogHandler inMemoryLogHandler = new InMemoryLogHandler(
            record -> record.getLevel().intValue() >= Level.ALL.intValue());

    static {
        rootLogger.addHandler(inMemoryLogHandler);
    }

    @BeforeEach
    public void setup() {
        inMemoryLogHandler.getRecords().clear();
    }

    public static class TestLoggerLevelProfile implements QuarkusTestProfile {

        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of("quarkus.log.level", "DEBUG");
        }
    }

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(is("Hello from RESTEasy Reactive"));

        Assertions.assertTrue(inMemoryLogHandler.getRecords().stream()
                .anyMatch(logRecord -> logRecord.getMessage().equals("info message")));

        // Fails under continuous testing dev mode but passed in test mode
        Assertions.assertTrue(inMemoryLogHandler.getRecords().stream()
                .anyMatch(logRecord -> logRecord.getMessage().equals("debug message")));
    }

}