package application.domain.models;

public class UserID
{
    private long id;



    //constructors
    public UserID()
    {

    }
    public UserID(long id)
    {
        this.id = id;
    }



    //getters and setters
    public long getId() { return id; }

    public void setId(long id) { this.id = id; }
}