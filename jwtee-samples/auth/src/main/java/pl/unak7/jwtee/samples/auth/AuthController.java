package pl.unak7.jwtee.samples.auth;

import pl.unak7.jwtee.JWTSessionManager;
import pl.unak7.jwtee.samples.auth.dao.UserDao;
import pl.unak7.jwtee.samples.auth.domain.User;
import pl.unak7.jwtee.samples.auth.dto.AuthRequestDTO;
import pl.unak7.jwtee.samples.auth.dto.AuthResponseDTO;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/auth")
public class AuthController {

    @Inject
    private UserDao userDao;

    @Inject
    private JWTSessionManager sessionManager;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(AuthRequestDTO authRequest) {
        Optional<User> user = userDao.getByUsername(authRequest.getUsername());
        if(user.isPresent() && user.get().getPassword().equals(authRequest.getPassword())) {
            sessionManager.setAttribute("username", user.get().getUsername());
            AuthResponseDTO response = new AuthResponseDTO(true, sessionManager.getToken(), user.get().getFirstName(), user.get().getCounter());
            return Response.status(Response.Status.OK).entity(response).build();
        }
        return Response.status(Response.Status.OK).entity(new AuthResponseDTO(false, null, null, null)).build();
    }
}
