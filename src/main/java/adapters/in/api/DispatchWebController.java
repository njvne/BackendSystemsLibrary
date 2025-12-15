package adapters.in.api;

import adapters.in.api.utils.Hyperlinks;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.*;
import java.util.Arrays;
import java.util.Base64;


@Path("library")
public class DispatchWebController
{
    @Context
    private UriInfo uriInfo;

    @Context
    protected HttpServletRequest request;



    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllBooks()
    {
        //String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final Response.ResponseBuilder builder = Response.ok("links haha");
        Hyperlinks.addLink(uriInfo, builder, "/library/books", "getAllBooks", "application/json");
        Hyperlinks.addLink(uriInfo, builder, "/library/users/create", "UserCreation", "application/json");
        //how to manage logins? if dispatch is called with login data, send correlating hyperlinks?

        /*if(authHeader != null)
        {   //from Unit26 video
            final String withoutBasic = authHeader.replaceFirst("(?i)basic ", "");
            final String userColonPass = Arrays.toString(Base64.getDecoder().decode(withoutBasic));
            //decodeAsString didnt exist, so i have to get a decoder, decode it to a byte array and then convert the byte array to string
            final String[] asArray = userColonPass.split(":", 2);
            final String username = asArray[0];
            final String password = asArray[1];

            //todo: check if authentication is valid. if so, send link to userprofile (and reservations if not integrated?). if logged in as admin, get link to post books etc.
        }*/



        //personal preference: user and borrow data is potentially sensitive data.
        //borrows can only be found by their respective user when logged in. Potentially an admin that can access all.
        //therefor: login or create user before giving a link to user data and borrows
        return builder.build();
    }
}