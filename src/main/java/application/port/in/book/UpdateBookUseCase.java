package application.port.in.book;

import application.domain.models.Book;
import application.domain.models.BookISBN;
import application.domain.results.NoContentResult;

public interface UpdateBookUseCase
{
    NoContentResult updateBook(BookISBN isbn, Book book);
}