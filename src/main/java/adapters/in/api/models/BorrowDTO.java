package adapters.in.api.models;

import application.domain.models.BookISBN;
import application.domain.models.UserID;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.DefaultValue;

import java.util.Date;

public class BorrowDTO extends AbstractDataTransferObject
{
    @NotBlank (message = "Borrow must include a book")
    private BookISBN isbn;
    @NotBlank (message = "Borrow must be tied to a user")
    private UserID userid;
    @NotBlank (message = "The borrows date must be included")
    private Date borrowdate;

    private boolean returned = false;


    public BookISBN getIsbn()
    {
        return isbn;
    }

    public void setIsbn(BookISBN isbn)
    {
        this.isbn = isbn;
    }

    public UserID getUserid()
    {
        return userid;
    }

    public void setUserid(UserID userid)
    {
        this.userid = userid;
    }

    public Date getBorrowdate()
    {
        return borrowdate;
    }

    public void setBorrowdate(Date borrowdate)
    {
        this.borrowdate = borrowdate;
    }

    public boolean isReturned()
    {
        return returned;
    }

    public void setReturned(boolean returned)
    {
        this.returned = returned;
    }
}