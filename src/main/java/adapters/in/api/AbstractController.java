package adapters.in.api;

import adapters.in.api.adapter.UserServiceAdapter;
import application.domain.Authorisation.AuthorizationLevel;
import application.domain.Authorisation.AuthorizationResult;
import adapters.in.api.Exceptions.MissingLoginDataException;
import adapters.in.api.Exceptions.WrongCredentialsException;
import adapters.in.api.utils.Hyperlinks;
import application.domain.UserService;
import io.quarkus.security.UnauthorizedException;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public abstract class AbstractController
{

    @Inject
    UserServiceAdapter userServiceAdapter;

    final String shortorlongpass = "badpassword";



    public AuthorizationResult checkAuthorizationLevel(HttpHeaders httpHeaders, long reqid)
    {
        try//necessary to process requests sent without auth
        {
            final String[] asArray = getUsernameAndPasswordAsArray(httpHeaders);
            final long userid = Long.parseLong(asArray[0]);
            final String password = asArray[1];
            if (userid > 0 && password != null)
            {
                AuthorizationResult r = this.userServiceAdapter.checkAuth(userid, password);
                if(reqid != r.getRelatedUserID())
                {
                    throw new UnauthorizedException("Unauthorized");
                }
                return r;
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
            final long userid = Long.parseLong(asArray[0]);
            final String password = asArray[1];
            if (userid > 0 && password != null)
            {
                return this.userServiceAdapter.checkAuth(userid, password);
            }
            else
            {
                throw new MissingLoginDataException();  //branch into not logged in
            }
        }
        catch(Exception e)     //->"not logged in"
        {
            AuthorizationLevel level = AuthorizationLevel.NOT_LOGGED_IN;
            return new AuthorizationResult(level, -1);
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
        Hyperlinks.addLink(uriInfo, builder, "/library/users" + u_id + "/borrows", "getAllOwnUserBorrows", "application/json");
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
            String[] res = userColonPass.split(":", 2);
            try{
                if(res[1].length() <= 8 || res[1].length() > 25)
                {
                    throw new WrongCredentialsException();
                }
                MessageDigest digest = MessageDigest.getInstance("SHA-256");                //hashing on server side
                res[1] = Arrays.toString(digest.digest(res[1].getBytes(StandardCharsets.UTF_8)));
                return res;
            }
            catch (NoSuchAlgorithmException e)
            {
                return new String[2];
            }
            catch (WrongCredentialsException e)
            {
                String[] ress = new String[2];
                ress[1] = shortorlongpass;
                return ress;
            }
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