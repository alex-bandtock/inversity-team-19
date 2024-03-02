package co.inversity.bh;

import co.inversity.bh.domain.RawData;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Path("/ingest/v1/api")
@Slf4j
@Produces(MediaType.APPLICATION_JSON)
public class IngestionResource {

    @POST
    @Transactional
    public Response ingest(RawData rawData) {
        log.info("Ingested new RawDate: {}", rawData);
        rawData.persist();
        return Response.ok().build();
    }
}
