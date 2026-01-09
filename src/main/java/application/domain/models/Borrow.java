package application.domain.models;
import java.util.Date;

public class Borrow
{
    private BookISBN isbn;
    private UserID userid;
    private Date borrowdate;
    private boolean returned;



    //constructor
    public Borrow() {}

    public Borrow(BookISBN isbn, UserID userid, boolean returned) {}



    //getters and setters
    public BookISBN getIsbn() { return isbn; }

    public void setIsbn(BookISBN isbn) { this.isbn = isbn; }

    public UserID getUserid() { return userid; }

    public void setUserid(UserID userid) { this.userid = userid; }

    public boolean isReturned() { return returned; }

    public void setReturned(boolean returned) { this.returned = returned; }
}