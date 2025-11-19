package application.domain.models;

public class CopyID
{
    private long id;



    //constructors
    public CopyID()
    {

    }
    public CopyID(long id)
    {
        this.id = id;
    }



    //getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}