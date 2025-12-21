package adapters.in.api;

import adapters.in.api.utils.Hyperlinks;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.*;
import java.util.Arrays;
import java.util.Base64;


@Path("/library")
public class DispatchWebController
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
        try//necessary to process requests sent without auth
        {
            final String authHeader = httpHeaders.getRequestHeader("Authorization").getFirst();
            if (authHeader != null && authHeader.startsWith("Basic"))
            {   //from Unit26 video
                final String withoutBasic = authHeader.replaceFirst("(?i)basic ", "");
                final String userColonPass = decodeAsString(withoutBasic);
                final String[] asArray = userColonPass.split(":", 2);
                final String username = asArray[0];
                final String password = asArray[1];
                if (username != null && password != null)
                {
                    //todo: check if authentication is valid.
                    int id = 0;
                    Hyperlinks.addLink(uriInfo, builder, "/library/users/" + id, "getUser", "application/json");
                }
            }
        }catch(Exception e){}
        Hyperlinks.addLink(uriInfo, builder, "/library/books{search}", "getAllBooks", "application/json");
        Hyperlinks.addLink(uriInfo, builder, "/library/users", "UserCreation", "application/json");
        return builder.build();

        //todo: check if authentication is valid. if so, send link to userprofile (and reservations if not integrated?). if logged in as admin, get link to post books etc.
        //personal preference: user and borrow data is potentially sensitive data.
        //borrows can only be found by their respective user when logged in. Potentially an admin that can access all.
        //therefor: login or create user before giving a link to user data and borrows
    }



    public String decodeAsString(String encoded)
    {
        //had to write this myself because apparently the example and some of its methods in unit 26 is not included in any of the dependencies
        //and also not anywhere I could find it. but it's not like the servletRequest worked. had to find that httpHeaders does the job.
        final String allBytes = Arrays.toString(Base64.getDecoder().decode(encoded));
        String[] byteValues = allBytes.substring(1, allBytes.length() - 1).split(",");
        byte[] bytes = new byte[byteValues.length];
        for(int i = 0; i < byteValues.length; i++)
        {
            bytes[i] = Byte.parseByte(byteValues[i].trim());
        }
        return new String(bytes);
    }
}