package adapters.in.api.models;

import application.domain.models.UserID;
import jakarta.validation.constraints.NotBlank;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Date;

@SuppressWarnings("SpellCheckingInspection")
@XmlRootElement(name = "user")
public class UserDTO extends AbstractDataTransferObject
{
    @NotBlank (message = "User must have a surname")
    private String name;
    @NotBlank (message = "User must have a first name")
    private String surname;
    @NotBlank (message = "User must have a phone number")
    private long phonenumber;
    @NotBlank (message = "User must have an email")
    private String email;
    @NotBlank (message = "User must have a birthday")
    private Date birthdate;


    public UserDTO()
    {
        super();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSurname()
    {
        return surname;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public long getPhonenumber()
    {
        return phonenumber;
    }

    public void setPhonenumber(long phonenumber)
    {
        this.phonenumber = phonenumber;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Date getBirthdate()
    {
        return birthdate;
    }

    public void setBirthdate(Date birthdate)
    {
        this.birthdate = birthdate;
    }
}