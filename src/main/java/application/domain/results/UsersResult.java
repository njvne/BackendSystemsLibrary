package application.domain.results;

import application.domain.models.User;

import java.util.List;

public class UsersResult extends CollectionModelResult<User>
{
    public UsersResult()
    {

    }

    public UsersResult(List<User> result)
    {
        super(result);
    }
}