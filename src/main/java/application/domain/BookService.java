package application.domain;

import application.domain.results.*;
import application.domain.models.Book;
import application.domain.models.BookISBN;
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

    @Inject
    CreateBookPort createBookPort;




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
        final var temp = this.readBookByIdPort.loadBookById(isbn);
        final var returnValue = new NoContentResult();

        if(temp.isEmpty())
        {
            final var result = this.createBookPort.createBook(book, isbn);
            if(result.hasError())
            {
                returnValue.setError(result.getErrorCode(), result.getErrorMessage());
            }
            else
            {
                returnValue.setError(PutStatus.CREATED, "Book created successfully");
            }
        }
        else if(temp.getResult().getIsbn().getISBN() != isbn.getISBN())
        {
            returnValue.setError(ErrorCodes.RESOURCE_ID_DOES_NOT_MATCH, "path id: " + isbn.getISBN() + " does not match resource id: " + temp.getResult().getIsbn().getISBN());
        }
        else
        {
            final var result = this.updateBookPort.updateBook(book, isbn);
            if(result.hasError())
            {
                returnValue.setError(result.getErrorCode(), result.getErrorMessage());
            }
            else
            {
                returnValue.setError(PutStatus.UPDATED, "Book updated successfully");
            }
        }
        return returnValue;
    }

    @Override
    public NoContentResult deleteBook(BookISBN ISBN) {
        final var result = this.deleteBookPort.deleteBook(ISBN);
        final var returnValue = new NoContentResult();
        if (result.hasError()) {
            returnValue.setError(result.getErrorCode(), result.getErrorMessage());
            return returnValue;
        } else {
            return result;
        }
    }
}