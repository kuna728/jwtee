package pl.unak7.jwtee.sample;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/item")
public class ItemWebService {

    @GET
    public Response listItems() {
        return Response.ok("Hejka").build();
    }
}
