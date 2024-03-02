package co.inversity.bh;

import co.inversity.bh.domain.RawData;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class DataInterpreterTest {

    @Inject
    DataInterpreter dataInterpreter;

    @Test
    public void when_interpreting_withUnbalanced_thenReturn() {
        Set<String> insights = dataInterpreter.status(RawData.builder()
                        .payload("""
                                {
                                    "soc": 69.12,
                                    "outputVoltage": 462.16,
                                    "chargeRate": 0.84,
                                    "temperature": 45.24,
                                    "chargeCycles": 403,
                                    "cellBalance": "unbalanced",
                                    "batteryAge": 1369
                                }
                                """)
                .build());

        assertEquals(1, insights.size());
        assertEquals("Cells are unbalanced", insights.stream().findAny().orElse(""));
    }

    @Test
    public void when_interpreting_withTemp_thenReturn() {
        Set<String> insights = dataInterpreter.status(RawData.builder()
                        .payload("""
                                {
                                    "soc": 69.12,
                                    "outputVoltage": 462.16,
                                    "chargeRate": 0.84,
                                    "temperature": 55.24,
                                    "chargeCycles": 403,
                                    "cellBalance": "balanced",
                                    "batteryAge": 1369
                                }
                                """)
                .build());

        assertEquals(1, insights.size());
        assertEquals("Temperature too high", insights.stream().findAny().orElse(""));
    }

    @Test
    public void when_interpreting_withBatteryOld_thenReturn() {
        Set<String> insights = dataInterpreter.status(RawData.builder()
                        .payload("""
                                {
                                    "soc": 69.12,
                                    "outputVoltage": 462.16,
                                    "chargeRate": 0.84,
                                    "temperature": 45.24,
                                    "chargeCycles": 403,
                                    "cellBalance": "balanced",
                                    "batteryAge": 3000
                                }
                                """)
                .build());

        assertEquals(1, insights.size());
        assertEquals("Battery is older than 6 years", insights.stream().findAny().orElse(""));
    }

    @Test
    public void when_interpreting_withCycles_thenReturn() {
        Set<String> insights = dataInterpreter.status(RawData.builder()
                        .payload("""
                                {
                                    "soc": 69.12,
                                    "outputVoltage": 462.16,
                                    "chargeRate": 0.84,
                                    "temperature": 45.24,
                                    "chargeCycles": 15000,
                                    "cellBalance": "balanced",
                                    "batteryAge": 1000
                                }
                                """)
                .build());

        assertEquals(1, insights.size());
        assertEquals("Battery has had excessive charge cycles", insights.stream().findAny().orElse(""));
    }
}