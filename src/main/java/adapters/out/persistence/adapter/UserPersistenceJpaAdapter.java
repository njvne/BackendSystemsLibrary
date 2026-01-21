package adapters.out.persistence.adapter;

import adapters.out.persistence.Mapper;
import adapters.out.persistence.models.UserAuthJpaEntity;
import adapters.out.persistence.models.UserJpaEntity;
import application.domain.Authorisation.AuthorizationLevel;
import application.domain.Authorisation.AuthorizationResult;
import application.domain.models.User;
import application.domain.models.UserID;
import application.domain.results.ErrorCodes;
import application.domain.results.NoContentResult;
import application.domain.results.UserResult;
import application.port.out.FindAuthorisationPort;
import application.port.out.user.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


@ApplicationScoped
public class UserPersistenceJpaAdapter implements PersistUserPort, ReadUserByIdPort, FindAuthorisationPort
{
    @Inject
    private EntityManager em;


    private Mapper mapper = new Mapper();



    @Override
    @Transactional
    public NoContentResult createUser(User user, String hashedPass)
    {
        try
        {
            //transaction.begin
            final var entitymodel = this.mapper.userToEntity(user);
            this.em.persist(entitymodel);
            this.em.flush();
            user.setUserid(new UserID(entitymodel.getUserid()));
            UserAuthJpaEntity auth = new UserAuthJpaEntity(entitymodel, hashedPass, AuthorizationLevel.USER);
            this.em.persist(auth);
            this.em.flush();
            //transaction.commit
        }
        catch(Exception e)
        {
            NoContentResult result = new NoContentResult();
            result.setError();
            return result;
        }
        return new NoContentResult();
    }

    @Override
    public UserResult readUserById(long id)
    {
        final var entitymodel = this.em.find(UserJpaEntity.class, id);
        if(entitymodel == null)
        {
            UserResult result = new UserResult();
            result.setError(ErrorCodes.RESOURCE_NOT_FOUND, "User with id " + id + " not found");
            return result;
        }
        return new UserResult(this.mapper.userToDomain(entitymodel));
    }

    @Override
    public AuthorizationResult checkPasswordCombination(long uid, String hashedPass)
    {
        TypedQuery<UserAuthJpaEntity> find = this.em.createQuery("FROM UserAuthJpaEntity ua WHERE ua.user.userid = :usid", UserAuthJpaEntity.class);
        find.setParameter("usid", uid);

        final var res = find.getSingleResult();

        if(res == null ||!res.getHashedpass().equals(hashedPass))
        {
            return new AuthorizationResult(AuthorizationLevel.NOT_LOGGED_IN, -1);
        }
        else
        {
            return new AuthorizationResult(res.getAuthorizationLevel(), res.getId());
        }
    }

    @Transactional
    protected void createAdminUser(UserJpaEntity user, UserAuthJpaEntity auth)
    {
        this.em.persist(user);
        this.em.persist(auth);
        this.em.flush();
    }



    @PostConstruct
    protected void createAdmin()
    {
        UserJpaEntity ad = new UserJpaEntity();
        ad.setName("Admin");
        ad.setSurname("Armin");
        ad.setEmail("admin@admin.com");
        ad.setPhonenumber(4417033545L);
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String pass = Arrays.toString(digest.digest("adminpassword123".getBytes(StandardCharsets.UTF_8)));
            UserAuthJpaEntity ada = new UserAuthJpaEntity(ad, pass, AuthorizationLevel.ADMIN);
            createAdminUser(ad, ada);
        }
        catch (NoSuchAlgorithmException e) {}
    }
}