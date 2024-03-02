package co.inversity.bh;

import co.inversity.bh.domain.Battery;
import co.inversity.bh.domain.BatteryDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Path("/bh/api/v1")
@Produces(MediaType.APPLICATION_JSON)
public class QueryResource {

    @GET
    @Path("/{serialNumber}")
    public Response getBattery(@PathParam("serialNumber") String serialNumber) {
        Optional<Battery> batteryOpt = Battery.findBySerialNumber(serialNumber);
        if (batteryOpt.isEmpty()) {
            return Response.noContent().build();
        } else {
            Battery battery = batteryOpt.get();
            BatteryDTO dto = BatteryDTO.builder()
                    .serialNumber(battery.serialNumber)
                    .insights(battery.insights != null && battery.insights.length()  > 0 ? new HashSet<>(List.of(battery.insights.split("\\|"))) : null)
                    .build();
            return Response.ok(dto).build();
        }
    }
}
