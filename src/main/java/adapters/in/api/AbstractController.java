package adapters.in.api;

import adapters.in.api.utils.Hyperlinks;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.util.Arrays;
import java.util.Base64;

public abstract class AbstractController
{
    public void addLinkToUserIfAuthed(HttpHeaders httpHeaders, UriInfo uriInfo, Response.ResponseBuilder builder)
    {
        try//necessary to process requests sent without auth
        {
            final String[] asArray = getUsernameAndPasswordAsArray(httpHeaders);
            final String username = asArray[0];
            final String password = asArray[1];
            if (username != null && password != null)
            {
                //todo: check if authentication is valid. potentially include admin things too.
                //if else: if login failed, ??give link to creation??inform user of failed login by sending dispatch with rel name "tryLoginAgain"??, else, give link to users/id
                int id = 0;
                Hyperlinks.addLink(uriInfo, builder, "/library/users/" + id, "getUser", "application/json");
            }
        }catch(Exception e)
        {
            Hyperlinks.addLink(uriInfo, builder, "/library/users", "UserCreation", "application/json");
        }
    }

    public String[] getUsernameAndPasswordAsArray(HttpHeaders httpHeaders)
    {
        final String authHeader = httpHeaders.getRequestHeader("Authorization").getFirst();
        if (authHeader != null && authHeader.startsWith("Basic"))
        {
            final String withoutBasic = authHeader.replaceFirst("(?i)basic ", "");
            final String userColonPass = decodeAsString(withoutBasic);
            return userColonPass.split(":", 2);
        }
        return new String[2];
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