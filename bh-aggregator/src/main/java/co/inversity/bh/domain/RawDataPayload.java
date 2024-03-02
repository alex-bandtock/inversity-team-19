package co.inversity.bh.domain;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public class RawDataPayload {
    public Double soc;
    public Integer outputVoltage;
    public Integer chargeRate;
    public Integer temperature;
    public Integer chargeCycles;
    public String cellBalance;
    public Long batteryAge;
}
