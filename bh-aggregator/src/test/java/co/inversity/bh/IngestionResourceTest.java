package co.inversity.bh;

import co.inversity.bh.domain.RawData;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class IngestionResourceTest {

    @Test
    public void when_Ingesting_thenPersist() {
        given()
                .body("""
                        {"time": "2024-03-02T14:59:03.568136", "serialNumber": "BT001", "payload": "{\\"soc\\": 73.14, \\"outputVoltage\\": 441.34, \\"chargeRate\\": 0.56, \\"temperature\\": 52.9, \\"chargeCycles\\": 808, \\"cellBalance\\": \\"unbalanced\\", \\"batteryAge\\": 47}"}
                        """)
                .header("content-type", "application/json")
                .when().post("/ingest/v1/api")
                .then()
                .statusCode(200);

        assertEquals(1, RawData.unprocessed().size());
    }
}