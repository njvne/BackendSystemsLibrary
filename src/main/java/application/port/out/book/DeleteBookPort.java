package application.port.out.book;

import application.domain.models.Book;
import application.domain.models.BookISBN;
import application.domain.results.NoContentResult;

public interface DeleteBookPort
{
    NoContentResult deleteBook(BookISBN bookISBN);
}