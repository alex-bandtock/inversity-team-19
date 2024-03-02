package co.inversity.bh;

import co.inversity.bh.domain.Battery;
import co.inversity.bh.domain.RawData;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class AggregationServiceTest {

    @Inject
    AggregationService aggregationService;

    @Test
    public void when_AggregatingNewBattery_thenPersist() {
        createRawData("serNo", """
                {
                    "soc": 69.12,
                    "outputVoltage": 462.16,
                    "chargeRate": 0.84,
                    "temperature": 44.24,
                    "chargeCycles": 403,
                    "cellBalance": "balanced",
                    "batteryAge": 1369
                }
                """);

        assertEquals(RawData.unprocessed().size(), 1);

        aggregationService.aggregate();

        assertEquals(RawData.unprocessed().size(), 0);
        assertTrue(Battery.findBySerialNumber("serNo").isPresent());
    }

    @Test
    public void when_AggregatingNewBattery_withIssue_thenPersistInsight() {
        createRawData("serNo", """
                {
                    "soc": 69.12,
                    "outputVoltage": 462.16,
                    "chargeRate": 0.84,
                    "temperature": 55.24,
                    "chargeCycles": 403,
                    "cellBalance": "balanced",
                    "batteryAge": 1369
                }
                """);

        assertEquals(RawData.unprocessed().size(), 1);

        aggregationService.aggregate();

        assertEquals(RawData.unprocessed().size(), 0);
        assertTrue(Battery.findBySerialNumber("serNo").isPresent());
        assertTrue(Battery.findBySerialNumber("serNo").get().insights.contains("Temperature too high"));
    }

    @Transactional
    public void createRawData(String serialNumber, String payload) {
        RawData rawData = RawData.builder()
                .serialNumber(serialNumber)
                .payload(payload)
                .time("2024-01-01T00:00:00.000")
                .processed(false)
                .build();

        rawData.persist();
    }

}