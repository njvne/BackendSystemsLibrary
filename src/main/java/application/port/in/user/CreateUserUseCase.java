package application.port.in.user;

import application.domain.models.User;
import application.domain.results.NoContentResult;

public interface CreateUserUseCase
{
    NoContentResult create(User user, String password);
}