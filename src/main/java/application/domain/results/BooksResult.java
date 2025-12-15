package application.domain.results;

import application.domain.models.Book;
import java.util.List;


public class BooksResult extends CollectionModelResult<Book>
{
    public BooksResult()
    {

    }

    public BooksResult(List<Book> result)
    {
        super(result);
    }
}