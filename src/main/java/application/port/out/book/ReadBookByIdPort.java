package application.port.out.book;

import application.domain.models.BookISBN;
import application.domain.results.BookResult;

public interface ReadBookByIdPort
{
    BookResult loadBookById(BookISBN isbn);
}