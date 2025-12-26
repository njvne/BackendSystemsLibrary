package application.port.out.user;

import application.domain.results.UserResult;

public interface ReadUserByIdPort
{
    UserResult readUserById(long id);
}