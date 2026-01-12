package application.port.in.book;

import application.domain.models.BookISBN;
import application.domain.results.NoContentResult;

public interface DeleteBookUseCase
{
    NoContentResult deleteBook(BookISBN bookISBN);
}