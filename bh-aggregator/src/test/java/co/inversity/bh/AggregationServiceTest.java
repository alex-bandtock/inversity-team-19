package co.inversity.bh;

import co.inversity.bh.domain.RawData;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class AggregationServiceTest {

    @Inject
    AggregationService aggregationService;

    @Test
    public void when_AggregatingNewBattery_thenPersist() {
        createRawData("serNo", """
                {
                }
                """);

        aggregationService.aggregate();

        RawData.listAll().stream().anyMatch()
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