package application.domain.models;

public class ReservationID
{
    private long res_id;



    //constructors
    public ReservationID()
    {

    }
    public ReservationID(long res_id)
    {
        this.res_id = res_id;
    }



    //getters and setters
    public long getRes_id() { return res_id; }

    public void setRes_id(long res_id) { this.res_id = res_id; }
}