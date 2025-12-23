package adapters.in.api.Authorisation;

public class AuthorizationResult
{
    private AuthorizationLevel level;
    private long userid;

    public AuthorizationResult(AuthorizationLevel level, long userid)
    {
        this.level = level;
        this.userid = userid;
    }

    public AuthorizationLevel getAuthorizationLevel()
    {
        return level;
    }

    public long getRelatedUserID()
    {
        return userid;
    }

    //no setters as we do not want changes from outside.
}