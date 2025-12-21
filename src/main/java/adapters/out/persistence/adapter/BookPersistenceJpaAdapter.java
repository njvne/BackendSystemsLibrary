package adapters.out.persistence.adapter;

import application.domain.models.Book;
import application.domain.models.BookISBN;
import application.domain.results.BookResult;
import application.domain.results.BooksResult;
import application.domain.results.NoContentResult;
import application.port.out.book.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;


@ApplicationScoped
public class BookPersistenceJpaAdapter implements DeleteBookPort, PersistBookPort, ReadAllBooksPort, ReadBookByIdPort, ReadBooksByFilterPort, UpdateBookPort
{
    @Inject
    EntityManager em;

    @Override
    public NoContentResult deleteBook(BookISBN bookISBN)
    {
        return null;
    }

    @Override
    public NoContentResult persistBook(Book book, BookISBN isbn)
    {
        return null;
    }

    @Override
    public BooksResult loadAllBooks()
    {
        return null;
    }

    @Override
    public BookResult loadBookById(BookISBN isbn)
    {
        return null;
    }

    @Override
    public BooksResult loadBookByFilter(int page, String query)
    {
        return null;
    }

    @Override
    public NoContentResult updateBook(Book book)
    {
        return null;
    }
}