package application.port.in.book;

import application.domain.models.Book;
import application.domain.results.NoContentResult;

public interface CreateBookUseCase
{
    NoContentResult createBook(Book book);
}