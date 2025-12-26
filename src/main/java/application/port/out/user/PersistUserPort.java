package application.port.out.user;

import application.domain.models.User;
import application.domain.results.NoContentResult;

public interface PersistUserPort
{
    NoContentResult createUser(User user);
}