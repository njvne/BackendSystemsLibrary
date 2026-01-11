package application.domain.models;
import java.util.Date;

public class Borrow
{
    private long id;
    private BookISBN isbn;
    private UserID userid;
    private boolean returned;



    //constructor
    public Borrow() {}

    public Borrow(BookISBN isbn, UserID userid, boolean returned) {}



    //getters and setters


    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public BookISBN getIsbn() { return isbn; }

    public void setIsbn(BookISBN isbn) { this.isbn = isbn; }

    public UserID getUserid() { return userid; }

    public void setUserid(UserID userid) { this.userid = userid; }

    public boolean isReturned() { return returned; }

    public void setReturned(boolean returned) { this.returned = returned; }
}