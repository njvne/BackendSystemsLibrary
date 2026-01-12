package application.domain;

import application.domain.results.PutStatus;
import application.domain.models.Book;
import application.domain.models.BookISBN;
import application.domain.results.BookResult;
import application.domain.results.BooksResult;
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
        final var returnValue = new NoContentResult();
        final var result = this.updateBookPort.updateOrPersistBook(book, isbn);
        if(result.getErrorCode() == PutStatus.CREATED || result.getErrorCode() == PutStatus.UPDATED)
        {
            return result;
        }
        else
        {
            returnValue.setError(result.getErrorCode(), result.getErrorMessage());
            return returnValue;
        }
    }

    @Override
    public NoContentResult deleteBook(BookISBN ISBN)
    {
        final var result =  this.deleteBookPort.deleteBook(ISBN);
        final var returnValue = new NoContentResult();
        if(result.hasError())
        {
            returnValue.setError(result.getErrorCode(), result.getErrorMessage());
            return returnValue;
        }
        else
        {
            return result;
        }
    }

    @PostConstruct
    public void populateBook()
    {
        Book nu = new Book(new BookISBN(123456789));
        nu.setAuthor("amanda");
        nu.setDescription("This is the description");
        nu.setTitle("This is the title");
        nu.setCopyAmount(2);
        this.updateBookPort.updateOrPersistBook(nu, new BookISBN(123456789));
    }
}