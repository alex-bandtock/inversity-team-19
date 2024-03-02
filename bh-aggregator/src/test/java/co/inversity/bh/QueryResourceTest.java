package co.inversity.bh;

import co.inversity.bh.domain.Battery;
import co.inversity.bh.domain.RawData;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class QueryResourceTest {

    @Test
    public void when_Ingesting_thenPersist() {
        createBattery();

        given()
                .header("content-type", "application/json")
                .when().get("/bh/v1/api/BT001")
                .then()
                .body(is("""
                        {"serialNumber":"BT001","insights":["Something","Some other thing"]}
                        """.trim()))
                .statusCode(200);

        assertEquals(1, RawData.unprocessed().size());
    }

    @Transactional
    public void createBattery() {
        Battery battery = Battery.builder()
                .serialNumber("BT001")
                .insights("Something|Some other thing")
                .build();

        battery.persist();
    }
}