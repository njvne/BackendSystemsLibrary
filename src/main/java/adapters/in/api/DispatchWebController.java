package adapters.in.api;

import application.domain.Authorisation.AuthorizationResult;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.*;


@Path("/library")
public class DispatchWebController extends AbstractController
{
    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders httpHeaders;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getDispatch()
    {
        final Response.ResponseBuilder builder = Response.ok();
        AuthorizationResult res = checkAuthorizationLevelWithoutId(httpHeaders);
        addDefaultLinksByAuthorizationLevel(uriInfo, builder, res);
        return builder.build();
    }
}