package application.domain;

import application.domain.models.Book;
import application.domain.models.BookISBN;
import application.domain.results.BookResult;
import application.domain.results.BooksResult;
import application.domain.results.ErrorCodes;
import application.domain.results.NoContentResult;
import application.port.in.book.*;
import application.port.out.book.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class BookService implements CreateBookUseCase, DeleteBookUseCase, LoadAllBooksUseCase, LoadBooksByFilterUseCase, LoadBookByIdUseCase, UpdateBookUseCase
{
    @Inject
    private PersistBookPort persistBookPort;

    @Inject
    private DeleteBookPort deleteBookPort;

    @Inject
    private ReadAllBooksPort readAllBooksPort;

    @Inject
    private ReadBookByIdPort readBookByIdPort;

    @Inject
    private ReadBooksByFilterPort readBooksByFilterPort;

    @Inject
    private UpdateBookPort updateBookPort;



    @Override
    public NoContentResult createBook(Book book)
    {
        return this.persistBookPort.persistBook(book);
    }

    @Override
    public NoContentResult deleteBook(BookISBN ISBN)
    {
        final var result =  this.readBookByIdPort.loadBookById(ISBN);
        final var returnValue = new NoContentResult();
        if(result.isEmpty())
        {
            returnValue.setError(ErrorCodes.RESOURCE_TO_DELETE_NOT_FOUND, "Book with ISBN '" + ISBN);
        }
        if(result.hasError())
        {
            returnValue.setError(result.getErrorCode(), result.getErrorMessage());
        }
        else
        {
            this.deleteBookPort.deleteBook(ISBN);
        }
        return returnValue;
    }

    @Override
    public BookResult loadBookByIsbn(BookISBN isbn)
    {
        return this.readBookByIdPort.loadBookById(isbn);
    }

    @Override
    public BooksResult loadAllBooks()
    {
        return this.readAllBooksPort.loadAllBooks();
    }

    @Override
    public BooksResult loadBookByFilter(String query)
    {
        return this.readBooksByFilterPort.loadBookByFilter(query);
    }

    @Override
    public NoContentResult updateBook(BookISBN isbn, Book book)
    {
        return null;
    }

}