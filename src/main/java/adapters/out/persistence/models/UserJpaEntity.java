package adapters.out.persistence.models;

import jakarta.persistence.*;

@Entity
public class UserJpaEntity
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int userid;

    private String name;
    private String surname;
    private long phonenumber;
    private String email;
    private String password;



    //getters and setters
    public int getUserid() { return userid; }

    public void setUserid(int userid) { this.userid = userid; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }

    public void setSurname(String surname) { this.surname = surname; }

    public long getPhonenumber() { return phonenumber; }

    public void setPhonenumber(long phonenumber) { this.phonenumber = phonenumber; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }
}