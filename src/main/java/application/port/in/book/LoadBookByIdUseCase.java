package application.port.in.book;

import application.domain.models.BookISBN;
import application.domain.results.BookResult;

public interface LoadBookByIdUseCase
{
    BookResult loadBookByIsbn(BookISBN isbn);
}