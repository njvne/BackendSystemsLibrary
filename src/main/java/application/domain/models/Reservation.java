package application.domain.models;
import java.util.Date;

public class Reservation
{
    private ReservationID reservationid;
    private CopyID copyid;
    private UserID userid;
    private Date expiryDate;
    private boolean PLACEHOLDER;



    //constructor
    public Reservation()
    {
        this.reservationid = new ReservationID();
    }



    //getters and setters
    public ReservationID getReservationid() { return reservationid; }

    public void setReservationid(ReservationID reservationid) { this.reservationid = reservationid; }

    public CopyID getCopyid() { return copyid; }

    public void setCopyid(CopyID copyid) { this.copyid = copyid; }

    public UserID getUserid() { return userid; }

    public void setUserid(UserID userid) { this.userid = userid; }

    public Date getExpiryDate() { return expiryDate; }

    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

    public boolean isPLACEHOLDER() { return PLACEHOLDER; }

    public void setPLACEHOLDER(boolean PLACEHOLDER) { this.PLACEHOLDER = PLACEHOLDER; }
}