package application.port.out.book;

import application.domain.results.BooksResult;

public interface ReadAllBooksPort
{
    BooksResult loadAllBooks();
}