package application.domain.results;

import application.domain.models.User;

public class UserResult extends SingleModelResult<User>
{
    public UserResult()
    {

    }

    public UserResult(User result)
    {
        super(result);
    }
}