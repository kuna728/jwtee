package pl.unak7.jwtee.samples.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import pl.unak7.jwtee.JWTSessionManager;
import pl.unak7.jwtee.samples.auth.dao.UserDao;
import pl.unak7.jwtee.samples.auth.domain.User;
import pl.unak7.jwtee.samples.auth.dto.CounterUpdateRequestDTO;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/counter")
public class CounterResource {

    @Inject
    private UserDao userDao;

    @Inject
    private JWTSessionManager sessionManager;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCounter(CounterUpdateRequestDTO counterUpdateRequest) {
        String username = sessionManager.getAttribute("username", new TypeReference<String>(){});
        if(username == null)
            return Response.status(Response.Status.UNAUTHORIZED).build();
        Optional<User> user = userDao.getByUsername(username);
        if(!user.isPresent())
            return Response.status(Response.Status.UNAUTHORIZED).build();
        user.get().setCounter(counterUpdateRequest.getCounter());
        userDao.save(user.get());
        return Response.status(Response.Status.OK).build();
    }
}
