package adapters.out.persistence;

import adapters.out.persistence.models.BookJpaEntity;
import application.domain.models.Book;
import application.domain.models.BookISBN;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper
{
    //todo

    public BookJpaEntity bookToEntity(Book book)
    {
        final var bookJpaEntity = new BookJpaEntity();
        bookJpaEntity.setAuthor(book.getAuthor());
        bookJpaEntity.setDescription(book.getDescription());
        bookJpaEntity.setIsbn(book.getIsbn().getISBN());
        bookJpaEntity.setTitle(book.getTitle());
        return bookJpaEntity;
    }

    public Book bookToDomain(BookJpaEntity bookJpa)
    {
        var book = new Book();
        book.setIsbn(new BookISBN(bookJpa.getIsbn()));
        System.out.println(bookJpa.getIsbn());
        System.out.println(book.getIsbn().getISBN());
        book.setAuthor(bookJpa.getAuthor());
        book.setDescription(bookJpa.getDescription());
        book.setTitle(bookJpa.getTitle());
        return book;
    }

    public List<Book> booksToDomainModels(List<BookJpaEntity> bookJpas)
    {
        return bookJpas.stream().map(this::bookToDomain).collect(Collectors.toList());
    }
}