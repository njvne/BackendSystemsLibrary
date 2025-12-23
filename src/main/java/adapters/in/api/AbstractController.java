package adapters.in.api;

import adapters.in.api.Authorisation.AuthorizationLevel;
import adapters.in.api.Authorisation.AuthorizationResult;
import adapters.in.api.Exceptions.MissingLoginDataException;
import adapters.in.api.Exceptions.WrongCredentialsException;
import adapters.in.api.utils.Hyperlinks;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.util.Arrays;
import java.util.Base64;

public abstract class AbstractController
{
    public AuthorizationResult checkAuthorizationLevel(HttpHeaders httpHeaders, long reqid)
    {
        try//necessary to process requests sent without auth
        {
            final String[] asArray = getUsernameAndPasswordAsArray(httpHeaders);
            final String username = asArray[0];
            final String password = asArray[1];
            if (username != null && password != null)
            {
                if(false) //todo: check if authentication is valid. potentially include admin things too.
                {
                    throw new WrongCredentialsException();
                }
                AuthorizationLevel level = AuthorizationLevel.USER;
                int id = 0;
                if(reqid != id)
                {
                    throw new WrongCredentialsException();
                }
                return new AuthorizationResult(level, id);
                //return 2; if admin
                //throw exception if incorrect credentials
            }
            else
            {
                throw new MissingLoginDataException();  //branch into not logged in
            }
        }
        catch(RuntimeException e)     //->"not logged in"
        {
            AuthorizationLevel level = AuthorizationLevel.NOT_LOGGED_IN;
            return new AuthorizationResult(level, -1);
        }
    }

    public AuthorizationResult checkAuthorizationLevelWithoutId(HttpHeaders httpHeaders)
    {
        try//necessary to process requests sent without auth
        {
            final String[] asArray = getUsernameAndPasswordAsArray(httpHeaders);
            final String username = asArray[0];
            final String password = asArray[1];
            if (username != null && password != null)
            {
                if(false) //todo: check if authentication is valid. potentially include admin things too.
                {
                    throw new WrongCredentialsException();
                }
                AuthorizationLevel level = AuthorizationLevel.USER;
                int id = 0;
                return new AuthorizationResult(level, id);
                //return 2; if admin
                //throw exception if incorrect credentials
            }
            else
            {
                throw new MissingLoginDataException();  //branch into not logged in
            }
        }
        catch(RuntimeException e)     //->"not logged in"
        {
            AuthorizationLevel level = AuthorizationLevel.NOT_LOGGED_IN;
            return new AuthorizationResult(level, -1);
        }
    }

    public AuthorizationLevel checkAuthorizationLevelDispatch(HttpHeaders httpHeaders, UriInfo uriInfo, Response.ResponseBuilder builder)
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
                //return 2; if admin
                return AuthorizationLevel.USER;
                //throw exception if incorrect credentials
            }
            else
            {
                throw new MissingLoginDataException();  //branch into not logged in
            }
        }
        catch(Exception e)     //->"not logged in"
        {
            return AuthorizationLevel.NOT_LOGGED_IN;
        }
    }



    public void addDefaultLinksByAuthorizationLevel(UriInfo uriInfo, Response.ResponseBuilder builder, AuthorizationResult res)
    {
        final AuthorizationLevel al = res.getAuthorizationLevel();
        final long u_id = res.getRelatedUserID();
        if(al == AuthorizationLevel.ADMIN)
        {
            addDefaultAdminHeaders(uriInfo, builder, u_id);
        }
        else if(al == AuthorizationLevel.USER)
        {
            addDefaultUserHeaders(uriInfo, builder, u_id);
        }
        else
        {
            addDefaultNotLoggedInHeaders(uriInfo, builder);
        }
    }

    public void addDefaultAdminHeaders(UriInfo uriInfo, Response.ResponseBuilder builder, long u_id)
    {
        //not gonna focus on adding things to admin that are shown implemented in user. alone + time limited
        Hyperlinks.addLink(uriInfo, builder, "/library/books{search}", "getAllBooks", "application/json");
        Hyperlinks.addLink(uriInfo, builder, "/library/books/{isbn}", "CreateNewBook", "application/json");
                                                                //fix informing admin about isbn trailing path element correctly
    }

    public void addDefaultUserHeaders(UriInfo uriInfo, Response.ResponseBuilder builder, long u_id)
    {
        Hyperlinks.addLink(uriInfo, builder, "/library/books{search}", "getAllBooks", "application/json");
        Hyperlinks.addLink(uriInfo, builder, "/library/users" + u_id, "getOwnUser", "application/json");
        Hyperlinks.addLink(uriInfo, builder, "/library/users" + u_id + "/reservations", "getAllOwnUserReservations", "application/json");
    }


    public void addDefaultNotLoggedInHeaders(UriInfo uriInfo, Response.ResponseBuilder builder)
    {
        Hyperlinks.addLink(uriInfo, builder, "/library/books{search}", "getAllBooks", "application/json");
        Hyperlinks.addLink(uriInfo, builder, "/library/users", "createNewUser", "application/json");
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