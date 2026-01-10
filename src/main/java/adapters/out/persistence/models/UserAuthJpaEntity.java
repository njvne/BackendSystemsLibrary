package adapters.out.persistence.models;

import application.domain.Authorisation.AuthorizationLevel;
import jakarta.persistence.*;
import jakarta.ws.rs.DefaultValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
public class UserAuthJpaEntity
{
    private static final Logger LOG = LoggerFactory.getLogger(UserAuthJpaEntity.class);

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne @JoinColumn(nullable = false, name = "relateduser")
    private UserJpaEntity user;

    @Column(nullable = false)
    private String hashedpass;

    @DefaultValue("User")
    private AuthorizationLevel authorizationLevel;


    public UserAuthJpaEntity(UserJpaEntity user, String hashedpass, AuthorizationLevel authorizationLevel)
    {
        this.user = user;
        this.hashedpass = hashedpass;
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

    public String getHashedpass()
    {
        return hashedpass;
    }

    public void setHashedpass(String hashedPassword)
    {
        this.hashedpass = hashedPassword;
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
