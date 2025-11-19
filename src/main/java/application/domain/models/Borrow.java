package application.domain.models;
import java.util.Date;

public class Borrow
{
    private BorrowID borrowid;
    private CopyID copyid;
    private UserID userid;
    private Date borrowdate;
    private boolean PLACEHOLDER;



    //constructor
    public Borrow()
    {
        this.borrowid = new BorrowID();
    }



    //getters and setters
    public BorrowID getBorrowid() { return borrowid; }

    public void setBorrowid(BorrowID borrowid) { this.borrowid = borrowid; }

    public CopyID getCopyid() { return copyid; }

    public void setCopyid(CopyID copyid) { this.copyid = copyid; }

    public UserID getUserid() { return userid; }

    public void setUserid(UserID userid) { this.userid = userid; }

    public Date getBorrowdate() { return borrowdate; }

    public void setBorrowdate(Date borrowdate) { this.borrowdate = borrowdate; }

    public boolean isPLACEHOLDER() { return PLACEHOLDER; }

    public void setPLACEHOLDER(boolean PLACEHOLDER) { this.PLACEHOLDER = PLACEHOLDER; }
}