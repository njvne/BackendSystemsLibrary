package application.port.out.book;

import application.domain.models.Book;
import application.domain.models.BookISBN;
import application.domain.results.NoContentResult;

public interface UpdateBookPort
{
    NoContentResult updateOrPersistBook(Book book, BookISBN isbn);
}