package co.inversity.bh;

import co.inversity.bh.domain.Battery;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

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
            return Response.ok(batteryOpt.get()).build();
        }
    }
}
