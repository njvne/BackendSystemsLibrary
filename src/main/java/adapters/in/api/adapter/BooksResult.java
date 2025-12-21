package adapters.in.api.adapter;

import adapters.in.api.models.BookDTO;
import java.util.List;

public class BooksResult
{
    private List<BookDTO> bookDTOs;



    public BooksResult()
    {

    }

    public BooksResult(List<BookDTO> bookDTOs)
    {
        this.bookDTOs = bookDTOs;
    }



    public List<BookDTO> getBookDTOs()
    {
        return bookDTOs;
    }
}