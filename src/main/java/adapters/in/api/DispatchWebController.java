package adapters.in.api;

import adapters.in.api.utils.Hyperlinks;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.*;


@Path("/library")
public class DispatchWebController extends AbstractController
{
    @Inject
    private Validator validator;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders httpHeaders;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getDispatch()
    {
        final Response.ResponseBuilder builder = Response.ok();
        addLinkToUserIfAuthed(httpHeaders, uriInfo, builder);
        Hyperlinks.addLink(uriInfo, builder, "/library/books{search}", "getAllBooks", "application/json");
        return builder.build();
        //personal preference: user and borrow data is potentially sensitive data.
        //borrows can only be found by their respective user when logged in. Potentially an admin that can access all.
        //therefor: login or create user before giving a link to user data and borrows
    }






}