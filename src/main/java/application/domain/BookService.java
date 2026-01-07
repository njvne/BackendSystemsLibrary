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
public class BookService implements DeleteBookUseCase, LoadAllBooksUseCase, LoadBooksByFilterUseCase, LoadBookByIdUseCase, UpdateBookUseCase
{

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
    public BookResult loadBookByIsbn(BookISBN isbn)
    {
        return this.readBookByIdPort.loadBookById(isbn);
    }

    @Override
    public BooksResult loadAllBooks(int page)
    {
        return this.readAllBooksPort.loadAllBooks(page);
    }

    @Override
    public BooksResult loadBookByFilter(int page, String query)
    {
        return this.readBooksByFilterPort.loadBookByFilter(page, query);
    }

    @Override
    public NoContentResult updateOrCreateBook(BookISBN isbn, Book book)
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
            this.updateBookPort.updateOrPersistBook(book, isbn);
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
    public void populateBook()
    {
        Book nu = new Book(new BookISBN(123456789));
        nu.setAuthor("amanda");
        nu.setDescription("This is the description");
        nu.setTitle("This is the title");
        this.updateBookPort.updateOrPersistBook(nu, new BookISBN(123456789));
    }
}