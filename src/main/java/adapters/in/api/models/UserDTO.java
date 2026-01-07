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
    private String phonenumber;
    @NotBlank (message = "User must have an email")
    private String email;


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

    public String getPhonenumber()
    {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber)
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
}