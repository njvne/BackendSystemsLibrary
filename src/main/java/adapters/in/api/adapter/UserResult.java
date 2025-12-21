package adapters.in.api.adapter;

import adapters.in.api.models.UserDTO;

public class UserResult
{
    private UserDTO UserDTO;



    public UserResult()
    {

    }

    public UserResult(UserDTO UserDTO)
    {
        this.UserDTO = UserDTO;
    }



    public UserDTO getUserDTO()
    {
        return UserDTO;
    }
}