package application.domain.models;
import java.util.Date;

public class Borrow
{
    private BookISBN isbn;
    private UserID userid;
    private Date borrowdate;
    private boolean PLACEHOLDER;



    //constructor
    public Borrow() {}



    //getters and setters
    public BookISBN getIsbn() { return isbn; }

    public void setCopyid(BookISBN isbn) { this.isbn = isbn; }

    public UserID getUserid() { return userid; }

    public void setUserid(UserID userid) { this.userid = userid; }

    public Date getBorrowdate() { return borrowdate; }

    public void setBorrowdate(Date borrowdate) { this.borrowdate = borrowdate; }

    public boolean isPLACEHOLDER() { return PLACEHOLDER; }

    public void setPLACEHOLDER(boolean PLACEHOLDER) { this.PLACEHOLDER = PLACEHOLDER; }
}