package application.port.out.book;

import application.domain.results.BooksResult;

public interface ReadBooksByFilterPort
{
    BooksResult loadBookByFilter(int page, String query);
}