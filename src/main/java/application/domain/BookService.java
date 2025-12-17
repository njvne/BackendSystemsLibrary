package application.domain;

import application.domain.models.Book;
import application.domain.models.BookISBN;
import application.domain.results.BookResult;
import application.domain.results.BooksResult;
import application.domain.results.ErrorCodes;
import application.domain.results.NoContentResult;
import application.port.in.book.*;
import application.port.out.book.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class BookService implements CreateBookUseCase, DeleteBookUseCase, LoadAllBooksUseCase, LoadBooksByFilterUseCase, LoadBookByIdUseCase, UpdateBookUseCase
{
    @Inject
    PersistBookPort persistBookPort;

    @Inject
    DeleteBookPort deleteBookPort;

    @Inject
    ReadAllBooksPort readAllBooksPort;

    @Inject
    ReadBookByIdPort readBookByIdPort;

    @Inject
    ReadBooksByFilterPort readBooksByFilterPort;

    @Inject
    UpdateBookPort updateBookPort;



    @Override
    public NoContentResult createBook(Book book, long isbn)
    {
        return this.persistBookPort.persistBook(book, new BookISBN(isbn));
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
    public BooksResult loadBookByFilter(int page, String query)
    {
        return this.readBooksByFilterPort.loadBookByFilter(page, query);
    }

    @Override
    public NoContentResult updateBook(BookISBN isbn, Book book)
    {
        final var result = this.readBookByIdPort.loadBookById(isbn);
        final var returnValue = new NoContentResult();
        if(result.isEmpty())
        {
            returnValue.setError(ErrorCodes.RESOURCE_TO_UPDATE_NOT_FOUND, "Book with ISBN '" + isbn.getISBN() + "' not found");
        }
        else if (isbn.getISBN() != book.getIsbn().getISBN())
        {
            returnValue.setError(ErrorCodes.RESOURCE_ID_DOES_NOT_MATCH, "Book with ISBN '" + isbn.getISBN() + "' does not match given update for " + book.getIsbn().getISBN());
        }
        else
        {
            this.updateBookPort.updateBook(book);
        }
        return returnValue;
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

    @PostConstruct
    public void populateBooks()
    {
        final var faker = new BookDataFaker();
        for(int i = 900000000; i < 900020000; i+=100)
        {
            this.createBook(faker.createModel(), i * 10L);
        }
    }
}