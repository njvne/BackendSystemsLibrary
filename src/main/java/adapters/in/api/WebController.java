package adapters.in.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("library/books")    //maybe seperate controllers for users, books, etc.? Refer to Unit 17 CRUD
public class WebController
{
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBooks()
    {
        //list of books = [load all books from database as json application]
        return Response.ok(/*list of books*/).build();
    }

    //papa
}