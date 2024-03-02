package co.inversity.bh;

import co.inversity.bh.domain.Battery;
import co.inversity.bh.domain.RawData;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class AggregationService {

    @Inject
    DataInterpreter dataInterpreter;

    @Scheduled(every = "10s")
    @Transactional
    public void aggregate() {
        for (RawData rawData : RawData.unprocessed()) {
            Optional<Battery> batteryOpt = Battery.findBySerialNumber(rawData.serialNumber);
            Battery battery = batteryOpt.orElse(Battery.builder()
                    .serialNumber(rawData.serialNumber)
                    .build());


            if (battery.insights == null) {
                battery.insights = new HashSet<>();
            }

            battery.insights.addAll(dataInterpreter.status(rawData));

            if (!battery.isPersistent()) {
                battery.persist();
            }
        }
    }
}
