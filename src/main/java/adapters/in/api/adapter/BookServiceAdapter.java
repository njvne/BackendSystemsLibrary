package adapters.in.api.adapter;

import adapters.in.api.models.BookDTO;
import application.domain.models.BookISBN;
import application.port.in.book.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.InternalServerErrorException;

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
}