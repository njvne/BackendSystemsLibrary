package application.domain.models;

public class BorrowID
{
    private long bor_id;



    //constructors
    public BorrowID()
    {

    }
    public BorrowID(long bor_id)
    {
        this.bor_id = bor_id;
    }



    //getters and setters
    public long getBor_id() { return bor_id; }

    public void setBor_id(long bor_id) { this.bor_id = bor_id; }
}