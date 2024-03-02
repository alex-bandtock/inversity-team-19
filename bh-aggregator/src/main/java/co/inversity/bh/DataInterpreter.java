package co.inversity.bh;

import co.inversity.bh.domain.Battery;
import co.inversity.bh.domain.RawData;
import co.inversity.bh.domain.RawDataPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class DataInterpreter {

    private Map<Predicate<RawDataPayload>, String> rules = Map.of(
            rdp -> "unbalanced".equals(rdp.cellBalance), "Cells are unbalanced",
            rdp -> rdp.temperature >= 50, "Temperature too high",
            rdp -> rdp.batteryAge >= 2190, "Battery is older than 6 years",
            rdp -> rdp.chargeCycles >= 10000, "Battery has had excessive charge cycles"
    );

    public Set<String> status(RawData rawData) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            final RawDataPayload rdp = objectMapper.readValue(rawData.payload, RawDataPayload.class);

            return rules.entrySet().stream()
                    .filter(e -> e.getKey().test(rdp))
                    .map(Map.Entry::getValue)
                    .peek(i -> log.info("Insight found: {}", i))
                    .collect(Collectors.toSet());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
