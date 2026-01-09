package adapters.out.persistence.models;

import application.domain.Authorisation.AuthorizationLevel;
import jakarta.persistence.*;
import jakarta.ws.rs.DefaultValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAuthJpaEntity
{
    private static final Logger LOG = LoggerFactory.getLogger(UserAuthJpaEntity.class);

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne @JoinColumn(nullable = false, name = "user")
    private UserJpaEntity user;

    @Column(nullable = false)
    private String hashedPassword;

    @DefaultValue("User")
    private AuthorizationLevel authorizationLevel;


    public UserAuthJpaEntity(UserJpaEntity user, String hashedPassword, AuthorizationLevel authorizationLevel)
    {
        this.user = user;
        this.hashedPassword = hashedPassword;
        this.authorizationLevel = authorizationLevel;
    }

    public UserAuthJpaEntity()
    {

    }



    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public UserJpaEntity getUser()
    {
        return user;
    }

    public void setUser(UserJpaEntity user)
    {
        this.user = user;
    }

    public String getHashedPassword()
    {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword)
    {
        this.hashedPassword = hashedPassword;
    }

    public AuthorizationLevel getAuthorizationLevel()
    {
        return authorizationLevel;
    }

    public void setAuthorizationLevel(AuthorizationLevel authorizationLevel)
    {
        LOG.info("Authorization level changed for User: {}, id: {} from: {} to: {}", user.getName(), user.getUserid(), this.authorizationLevel, authorizationLevel);
        if(authorizationLevel == AuthorizationLevel.NOT_LOGGED_IN)
        {
            throw new IllegalArgumentException("User has to be at least of User Authorization Level");
        }
        this.authorizationLevel = authorizationLevel;
    }
}
