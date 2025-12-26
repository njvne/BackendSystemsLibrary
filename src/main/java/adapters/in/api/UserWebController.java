package adapters.in.api;

import adapters.in.api.Authorisation.AuthorizationLevel;
import adapters.in.api.Authorisation.AuthorizationResult;
import adapters.in.api.models.UserDTO;
import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/library/users")
public class UserWebController extends AbstractController
{
    //borrows are a subresource 1:n -> implement hyperlinks for them under /user/{userid}/borrows

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders httpHeaders;


    @POST
    public Response createUser(@Valid UserDTO userDTO)
    {
        //todo: create user. if successful, do the below, if not, fail ig
        return Response.status(HttpResponseStatus.CREATED.code()).header("Location", createLocationHeader(new UserDTO()) + "/" + 0).build();
                                                                                            //todo: CHANGE DTO TO RESULT AND 0 TO NEW USERID
    }


    @GET
    @Path("/{uid}")
    public Response getUserInfo(@Positive @PathParam("uid") long uid)
    {
        final AuthorizationResult res = checkAuthorizationLevel(httpHeaders, uid);
        if(res.getAuthorizationLevel() == AuthorizationLevel.NOT_LOGGED_IN || uid != res.getRelatedUserID())
        {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        //todo: getuserbyId
        final Response.ResponseBuilder builder = Response.status(/*CHANGE TO RESULT*/Response.Status.OK);
        addDefaultLinksByAuthorizationLevel(uriInfo, builder, res);
        return Response.ok().build();
    }


    @GET
    @Path("/{uid}/borrows")
    public Response getUserBorrows(@Positive @PathParam("uid") long uid)
    {
        final AuthorizationResult res = checkAuthorizationLevel(httpHeaders, uid);
        if(res.getAuthorizationLevel() == AuthorizationLevel.NOT_LOGGED_IN || uid != res.getRelatedUserID())
        {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        //todo
        final Response.ResponseBuilder builder = Response.status(/*CHANGE TO RESULT*/Response.Status.OK);
        addDefaultLinksByAuthorizationLevel(uriInfo, builder, res);
        return Response.ok().build();
    }


    @GET
    @Path("/{uid}/borrows/{borrowNum}")
    public Response getSingleUserBorrow(@Positive @PathParam("uid") long uid, @Positive @PathParam("borrowNum") long borrowNum)
    {
        final AuthorizationResult res = checkAuthorizationLevel(httpHeaders, uid);
        if(res.getAuthorizationLevel() == AuthorizationLevel.NOT_LOGGED_IN || uid != res.getRelatedUserID())
        {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        final Response.ResponseBuilder builder = Response.status(/*CHANGE TO RESULT*/Response.Status.OK);
        addDefaultLinksByAuthorizationLevel(uriInfo, builder, res);
        return Response.ok().build();
    }


    @POST
    @Path("/{uid}/borrows/book/{isbn}")
    public Response borrowBook(@Positive @PathParam("uid") long uid, @Positive @PathParam("isbn") long isbn)
    {
        final AuthorizationResult res = checkAuthorizationLevel(httpHeaders, uid);
        if(res.getAuthorizationLevel() == AuthorizationLevel.NOT_LOGGED_IN || uid != res.getRelatedUserID())
        {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        final Response.ResponseBuilder builder = Response.status(/*CHANGE TO RESULT*/Response.Status.OK);
        addDefaultLinksByAuthorizationLevel(uriInfo, builder, res);
        return Response.ok().build();
    }
    //since I plan to include selflinks to the books in the list of reservations,
    //I don't see a reason to include search by SurrogateKey for reservations. Maybe by exact date or order by date or whatever as use case


    @PATCH
    @Path("/{uid}/borrows/{borrowNum}")
    public Response returnBook(@Positive @PathParam("uid") long uid, @Positive @PathParam("borrowNum") long borrowNum)
    {
        final AuthorizationResult res = checkAuthorizationLevel(httpHeaders, uid);
        if(res.getAuthorizationLevel() == AuthorizationLevel.NOT_LOGGED_IN || uid != res.getRelatedUserID())
        {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        final Response.ResponseBuilder builder = Response.status(/*CHANGE TO RESULT*/Response.Status.OK);
        addDefaultLinksByAuthorizationLevel(uriInfo, builder, res);
        return Response.ok().build();
    }

    
    private String createLocationHeader(UserDTO model)
    {
        return uriInfo.getRequestUriBuilder().path(Long.toString(model.getId())).build().toString();
    }
}