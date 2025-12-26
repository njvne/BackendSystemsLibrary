package adapters.in.api.adapter;

import adapters.in.api.Exceptions.IllegalUpdateException;
import adapters.in.api.Exceptions.ResourceConflictException;
import adapters.in.api.models.BookDTO;
import application.domain.models.BookISBN;
import application.domain.results.ErrorCodes;
import application.port.in.book.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class BookServiceAdapter
{
    @Inject
    private CreateBookUseCase createBookUseCase;

    @Inject
    private DeleteBookUseCase deleteBookUseCase;

    @Inject
    private LoadAllBooksUseCase loadAllBooksUseCase;

    @Inject
    private LoadBooksByFilterUseCase loadBookByFilterUseCase;

    @Inject
    private LoadBookByIdUseCase loadBookByIdUseCase;

    @Inject
    private UpdateBookUseCase updateBookUseCase;

    private Mapper mapper;



    public BookResult createNewBook(BookDTO bookModel, long isbn)
    {                                                                                                       //write isbnDTO???
        final var domainBookModel = this.mapper.bookDTOToDomainModel(bookModel);
        final var domainResult = this.createBookUseCase.createBook(domainBookModel, isbn);

        if (domainResult.hasError())
        {
            throw new InternalServerErrorException(domainResult.getErrorMessage());
        }
        else
        {
            return new BookResult(bookModel);
        }
    }

    public void deleteBook(long isbn)
    {
        final var domainResult = this.deleteBookUseCase.deleteBook(new BookISBN(isbn));
        if(domainResult.hasError())
        {
            if(domainResult.getErrorCode() == ErrorCodes.RESOURCE_TO_DELETE_NOT_FOUND)
            {
                throw new NotFoundException(domainResult.getErrorMessage());
            }
            else
            {
                throw new InternalServerErrorException(domainResult.getErrorMessage());
            }
        }
    }

    public BooksResult getAllBooks(int page)
    {
        final var domainResult = this.loadAllBooksUseCase.loadAllBooks();
        if(domainResult.hasError())
        {
            throw new InternalServerErrorException(domainResult.getErrorMessage());
        }
        else
        {
            return new BooksResult(this.mapper.booksToApiModels(domainResult.getResult()));
        }
    }

    public BooksResult getBooksByQuery(int page, String query)
    {
        final var domainResult = this.loadBookByFilterUseCase.loadBookByFilter(page, query);
        if(domainResult.hasError())
        {
            throw new InternalServerErrorException(domainResult.getErrorMessage());
        }
        else
        {
            return new BooksResult(this.mapper.booksToApiModels(domainResult.getResult()));
        }
    }

    public BookResult loadBookById(long isbn)
    {
        final var domainResult = this.loadBookByIdUseCase.loadBookByIsbn(new BookISBN(isbn));
        if(domainResult.hasError())
        {
            if (domainResult.getErrorCode() == ErrorCodes.RESOURCE_NOT_FOUND)
            {
                throw new NotFoundException(domainResult.getErrorMessage());
            }
            else
            {
                throw new InternalServerErrorException(domainResult.getErrorMessage());
            }
        }
        else
        {
            return new BookResult(this.mapper.bookToApiModel(domainResult.getResult()));
        }
    }

    public void updateBook(long isbn, BookDTO bookModel)
    {
        final var domainBook = this.mapper.bookDTOToDomainModel(bookModel);
        final var domainResult = this.updateBookUseCase.updateBook(new BookISBN(isbn), domainBook);
        if(domainResult.hasError())
        {
            if(domainResult.getErrorCode() == ErrorCodes.RESOURCE_TO_UPDATE_NOT_FOUND)
            {
                throw new NotFoundException(domainResult.getErrorMessage());
            }
            if(domainResult.getErrorCode() == ErrorCodes.RESOURCE_ID_DOES_NOT_MATCH)
            {
                throw new IllegalUpdateException(domainResult.getErrorMessage());
            }
            if(domainResult.getErrorCode() == ErrorCodes.RESOURCE_CONFLICT)
            {
                throw new ResourceConflictException(domainResult.getErrorMessage());
            }
            throw new InternalServerErrorException(domainResult.getErrorMessage());
        }
    }
}