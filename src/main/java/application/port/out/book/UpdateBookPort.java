package application.port.out.book;

import application.domain.models.Book;
import application.domain.results.NoContentResult;

public interface UpdateBookPort
{
    NoContentResult updateBook(Book book);
}