package co.inversity.bh.domain;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Set;

@Data
@Builder
@Jacksonized
public class BatteryDTO {

    public String serialNumber;
    public Set<String> insights;
}
