package application.domain.models;

public class User {
    private UserID userid;
    private String name;
    private String surname;
    private long phonenumber;
    private String email;
    //password?



    //constructor
    public User() {
        userid = new UserID();
    }



    //getters and setters
    public UserID getUserid() { return userid; }

    public void setUserid(UserID userid) { this.userid = userid; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }

    public void setSurname(String surname) { this.surname = surname; }

    public long getPhonenumber() { return phonenumber; }

    public void setPhonenumber(long phonenumber) { this.phonenumber = phonenumber; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }
}