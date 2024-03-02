package co.inversity.bh;

import co.inversity.bh.domain.RawData;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/ingest/v1/api")
public class IngestionResource {

    @POST
    public Response ingest(RawData rawData) {
        rawData.persist();
        return Response.ok().build();
    }
}
