package application.domain.results;

import application.domain.models.Book;

public class BookResult extends SingleModelResult<Book>
{
    public BookResult()
    {

    }

    public BookResult(Book result)
    {
        super(result);
    }
}