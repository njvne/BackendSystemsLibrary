package application.port.in.book;

import application.domain.results.BooksResult;

public interface LoadAllBooksUseCase
{
    BooksResult loadAllBooks(int page);
}