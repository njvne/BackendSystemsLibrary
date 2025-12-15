package adapters.in.api;

import adapters.in.api.utils.Hyperlinks;
import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/library/users")
public class UserWebController
{
    //borrows are a subresource 1:n -> implement hyperlinks for them under /user/{userid}/borrows

    @Context
    private UriInfo uriInfo;


    @POST
    @Path("/create")
    public Response CreateUser()
    {
        //todo: create user. if successful, do the below, if not, fail ig
        final Response.ResponseBuilder builder = Response.status(Response.Status.CREATED);
        Hyperlinks.addLink(uriInfo, builder, "/library/user/" + String.valueOf(/*user id of new user*/ "placeholder"), "getUserInfo", "application/json");
        return Response
                .status(HttpResponseStatus.CREATED.code())
                .header("Location", "todo" /*create link to the dto/ new user*/)
                .build();
    }


    @GET
    @Path("/{uid: \\d+}")
    public Response GetUserInfo()
    {
        //todo
        return Response.ok().build();
    }

    @GET
    @Path("/{uid: \\d+}/reservations")
    public Response GetUserReservations()
    {
        //todo
        return Response.ok().build();
    }
    //since I plan to include selflinks to the books in the list of reservations,
    //I don't see a reason to include search by SurrogateKey for reservations. Maybe by exact date or order by date or whatever as use case
}