package adapters.in.api.models;

import application.domain.models.BookISBN;
import application.domain.models.UserID;
import jakarta.validation.constraints.NotBlank;


public class BorrowDTO extends AbstractDataTransferObject
{
    @NotBlank (message = "Borrow must include a book")
    private BookISBN isbn;
    @NotBlank (message = "Borrow must be tied to a user")
    private UserID userid;

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

    public boolean isReturned()
    {
        return returned;
    }

    public void setReturned(boolean returned)
    {
        this.returned = returned;
    }
}