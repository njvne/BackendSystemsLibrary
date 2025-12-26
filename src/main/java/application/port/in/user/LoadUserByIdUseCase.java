package application.port.in.user;

import application.domain.results.UserResult;

public interface LoadUserByIdUseCase
{
    UserResult loadUserById(long id);
}