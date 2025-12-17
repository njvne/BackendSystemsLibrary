package application.port.in.book;

import application.domain.results.BooksResult;

public interface LoadBooksByFilterUseCase
{
    BooksResult loadBookByFilter(int page, String query);
}