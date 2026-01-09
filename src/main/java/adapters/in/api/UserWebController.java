package adapters.in.api;

import adapters.in.api.adapter.UserServiceAdapter;
import adapters.in.api.models.AbstractDataTransferObject;
import adapters.in.api.models.BookDTO;
import adapters.in.api.models.BorrowDTO;
import application.domain.Authorisation.AuthorizationLevel;
import application.domain.Authorisation.AuthorizationResult;
import adapters.in.api.models.UserDTO;
import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.List;

@Path("/library/users")
public class UserWebController extends AbstractController
{
    //borrows are a subresource 1:n -> implement hyperlinks for them under /user/{userid}/borrows

    @Inject
    UserServiceAdapter userServiceAdapter;

    @Inject
    private Validator validator;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders httpHeaders;


    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createUser(@Valid UserDTO userDTO)
    {
        String[] temp = getUsernameAndPasswordAsArray(httpHeaders);
        String pass = temp[1];
        if(pass.length() <= 8 || pass.length() > 25)
        {
            final Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST).entity("Password must be between 8 and 25 characters.");
            addDefaultNotLoggedInHeaders(uriInfo, builder);
            return builder.build();
        }
        final var result = this.userServiceAdapter.createUser(userDTO, pass);
        final Response.ResponseBuilder builder = Response.status(Response.Status.CREATED).header("Location", createLocationHeader(result.getUserDTO()));
        addDefaultUserHeaders(uriInfo, builder, result.getUserDTO().getId());
        return builder.build();
    }


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{uid}")
    public Response getUserInfo(@Positive @PathParam("uid") long uid)
    {
        final AuthorizationResult res = checkAuthorizationLevel(httpHeaders, uid);
        if(res.getAuthorizationLevel() == AuthorizationLevel.NOT_LOGGED_IN || (res.getAuthorizationLevel() == AuthorizationLevel.USER && uid != res.getRelatedUserID()))
        {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        final var result = this.userServiceAdapter.getUserById(uid);
        final Response.ResponseBuilder builder = Response.ok(result);
        addSelfLinkToDTO(result.getUserDTO());
        addDefaultLinksByAuthorizationLevel(uriInfo, builder, res);
        return builder.build();
    }


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{uid}/borrows")
    public Response getUserBorrows(@Positive @PathParam("uid") long uid)
    {
        final AuthorizationResult res = checkAuthorizationLevel(httpHeaders, uid);
        if(res.getAuthorizationLevel() == AuthorizationLevel.NOT_LOGGED_IN || (res.getAuthorizationLevel() == AuthorizationLevel.USER && uid != res.getRelatedUserID()))
        {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        final var result = this.userServiceAdapter.getAllBorrows(uid);
        final Response.ResponseBuilder builder = Response.ok(result);
        addSelfLinksToDTOs(result.getBorrowDTOs());
        addDefaultLinksByAuthorizationLevel(uriInfo, builder, res);
        return builder.build();
    }


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{uid}/borrows/{borrowNum}")
    public Response getSingleUserBorrow(@Positive @PathParam("uid") long uid, @Positive @PathParam("borrowNum") long borrowNum)
    {
        final AuthorizationResult res = checkAuthorizationLevel(httpHeaders, uid);
        if(res.getAuthorizationLevel() == AuthorizationLevel.NOT_LOGGED_IN || (res.getAuthorizationLevel() == AuthorizationLevel.USER && uid != res.getRelatedUserID()))
        {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        final var result = this.userServiceAdapter.getBorrowByNumber(uid, borrowNum);
        final Response.ResponseBuilder builder = Response.ok(result);
        addSelfLinkToDTO(result.getBorrow());
        addDefaultLinksByAuthorizationLevel(uriInfo, builder, res);
        return builder.build();
    }


    @POST
    @Path("/{uid}/borrows/{isbn}")
    public Response borrowBook(@Positive @PathParam("uid") long uid, @Positive @PathParam("isbn") long isbn)
    {
        final AuthorizationResult res = checkAuthorizationLevel(httpHeaders, uid);
        if(res.getAuthorizationLevel() == AuthorizationLevel.NOT_LOGGED_IN || (res.getAuthorizationLevel() == AuthorizationLevel.USER && uid != res.getRelatedUserID()))
        {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        final var result = this.userServiceAdapter.createBorrow(uid, isbn);
        final Response.ResponseBuilder builder = Response.status(Response.Status.CREATED).header("Location", createBorrowLocationHeader(result.getBorrow()));
        addSelfLinkToDTO(result.getBorrow());
        addDefaultLinksByAuthorizationLevel(uriInfo, builder, res);
        return builder.build();
    }
    //since I plan to include selflinks to the books in the list of reservations,
    //I don't see a reason to include search by SurrogateKey for reservations. Maybe by exact date or order by date or whatever as use case


    @PATCH
    @Path("/{uid}/borrows/{borrowNum}")
    public Response returnBook(@Positive @PathParam("uid") long uid, @Positive @PathParam("borrowNum") long borrowNum)
    {
        final AuthorizationResult res = checkAuthorizationLevel(httpHeaders, uid);
        if(res.getAuthorizationLevel() == AuthorizationLevel.NOT_LOGGED_IN || (res.getAuthorizationLevel() == AuthorizationLevel.USER && uid != res.getRelatedUserID()))
        {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        this.userServiceAdapter.returnBook(uid, borrowNum);
        final Response.ResponseBuilder builder = Response.noContent();
        addDefaultLinksByAuthorizationLevel(uriInfo, builder, res);
        return builder.build();
    }

    
    private String createLocationHeader(UserDTO model)
    {
        return uriInfo.getRequestUriBuilder().path(Long.toString(model.getId())).build().toString();
    }

    private String createBorrowLocationHeader(BorrowDTO model)
    {
        return uriInfo.getRequestUriBuilder().path(Long.toString(model.getId())).build().toString();
    }

    public void addSelfLinksToDTOs(List<? extends AbstractDataTransferObject> DTO)
    {
        DTO.forEach(this::addSelfLinkToDTO);
    }

    public void addSelfLinkToDTO(AbstractDataTransferObject dto)
    {
        final var currentUri = uriInfo.getAbsolutePath();
        final var path = currentUri.getPath();
        final var newPath = path.replaceFirst("/\\d*$", "");
        final var newUri = UriBuilder.fromUri(currentUri).replacePath(newPath + "/" + dto.getId()).build();
        dto.getSelfLink().setHref(newUri.toASCIIString());
        dto.getSelfLink().setRel("self");
        dto.getSelfLink().setType(httpHeaders.getHeaderString("Accept"));
    }
}