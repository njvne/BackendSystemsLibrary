package adapters.out.persistence.adapter;

import application.domain.models.User;
import application.domain.results.NoContentResult;
import application.domain.results.UserResult;
import application.port.out.user.PersistUserPort;
import application.port.out.user.ReadUserByIdPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class UserPersistenceJpaAdapter implements PersistUserPort, ReadUserByIdPort
{
    @Inject
    private EntityManager em;

    @Override
    public NoContentResult createUser(User user) {
        return null;
    }

    @Override
    public UserResult readUserById(long id) {
        return null;
    }
}