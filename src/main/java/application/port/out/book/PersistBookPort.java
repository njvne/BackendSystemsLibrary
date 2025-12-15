package application.port.out.book;

import application.domain.models.Book;
import application.domain.results.NoContentResult;

//corresponds to CreateBookUseCase


public interface PersistBookPort
{
    NoContentResult persistBook(Book book);
}