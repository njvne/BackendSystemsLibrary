package adapters.in.api.adapter;

import adapters.in.api.models.BookDTO;
import application.port.in.book.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.InternalServerErrorException;

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



    public BookResult createNewBook(BookDTO bookModel) {
        final var domainBookModel = this.mapper.bookDTOToDomainModel(bookModel);
        final var domainResult = this.createBookUseCase.createBook(domainBookModel);

        if (domainResult.hasError()) {
            throw new InternalServerErrorException(domainResult.getErrorMessage());
        } else {
            return new BookResult(bookModel);
        }
    }


}