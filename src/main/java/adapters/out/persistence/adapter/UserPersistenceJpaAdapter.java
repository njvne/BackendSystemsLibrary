package adapters.out.persistence.adapter;

import adapters.out.persistence.Mapper;
import adapters.out.persistence.models.UserAuthJpaEntity;
import application.domain.Authorisation.AuthorizationLevel;
import application.domain.models.Borrow;
import application.domain.models.User;
import application.domain.results.BorrowResult;
import application.domain.results.BorrowsResult;
import application.domain.results.NoContentResult;
import application.domain.results.UserResult;
import application.port.out.user.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class UserPersistenceJpaAdapter implements PersistUserPort, ReadUserByIdPort
{
    @Inject
    private EntityManager em;

    private Mapper mapper;

    @Override
    public NoContentResult createUser(User user, String hashedPass)
    {
        final var entitymodel = this.mapper.userToEntity(user);
        this.em.persist(entitymodel);
        UserAuthJpaEntity auth = new UserAuthJpaEntity(entitymodel, hashedPass, AuthorizationLevel.USER);
        return null;
    }

    @Override
    public UserResult readUserById(long id)
    {
        return null;
    }
}