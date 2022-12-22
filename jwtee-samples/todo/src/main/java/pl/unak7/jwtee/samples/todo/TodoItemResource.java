package pl.unak7.jwtee.samples.todo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.unak7.jwtee.JWTSessionManager;
import pl.unak7.jwtee.samples.todo.dto.NewItemDTO;
import pl.unak7.jwtee.samples.todo.dto.TodoItemDTO;
import pl.unak7.jwtee.samples.todo.dto.UpdateItemDTO;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Path("/item")
public class TodoItemResource {

    @Inject
    private JWTSessionManager sessionManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listItems() {
        return Response.status(Response.Status.OK).entity(getItemsFromToken()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addItem(NewItemDTO newItemDTO) {
        List<TodoItemDTO> items = getItemsFromToken();
        TodoItemDTO itemToAdd = new TodoItemDTO(items.size() + 1, newItemDTO.getName(), false);
        items.add(itemToAdd);
        try {
            sessionManager.put("items", items);
            return Response.status(Response.Status.CREATED).entity(itemToAdd).build();
        } catch (IOException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyItem(@PathParam("id") long id, UpdateItemDTO updateItemDTO) {
        List<TodoItemDTO> items = getItemsFromToken();
        Optional<TodoItemDTO> itemToUpdate = items.stream().filter(item -> item.getId() == id).findAny();
        if(!itemToUpdate.isPresent())
            return Response.status(Response.Status.NO_CONTENT).build();
        itemToUpdate.get().setDone(updateItemDTO.getDone());
        try {
            sessionManager.put("items", items);
            return Response.status(Response.Status.OK).entity(itemToUpdate).build();
        } catch (IOException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    private List<TodoItemDTO> getItemsFromToken() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<TodoItemDTO> items;
        try {
            items = (List<TodoItemDTO>) sessionManager.get("items", new TypeReference<List<TodoItemDTO>>(){});
        } catch (IOException e) {
            items = new ArrayList<>();
        }
        return items == null ? new ArrayList<>() : items;
    }

}
