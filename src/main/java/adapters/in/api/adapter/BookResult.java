package adapters.in.api.adapter;

import adapters.in.api.models.BookDTO;

public class BookResult
{
    private BookDTO bookDTO;



    public BookResult(){}

    public BookResult(BookDTO bookDTO)
    {
        this.bookDTO = bookDTO;
    }



    public BookDTO getBookDTO()
    {
        return bookDTO;
    }
}