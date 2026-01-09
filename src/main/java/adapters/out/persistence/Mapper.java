package adapters.out.persistence;

import adapters.out.persistence.models.BookJpaEntity;
import adapters.out.persistence.models.UserJpaEntity;
import application.domain.models.Book;
import application.domain.models.BookISBN;
import application.domain.models.User;
import application.domain.models.UserID;

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
        book.setAuthor(bookJpa.getAuthor());
        book.setDescription(bookJpa.getDescription());
        book.setTitle(bookJpa.getTitle());
        return book;
    }

    public UserJpaEntity userToEntity(User user)
    {
        final var userJpaEntity = new UserJpaEntity();
        userJpaEntity.setUserid(user.getUserid().getId());
        userJpaEntity.setName(user.getName());
        userJpaEntity.setEmail(user.getEmail());
        userJpaEntity.setSurname(user.getSurname());
        userJpaEntity.setPhonenumber(user.getPhonenumber());
        return userJpaEntity;
    }

    public User userToDomain(UserJpaEntity userJpaEntity)
    {
        final var user = new User();
        user.setUserid(new UserID(userJpaEntity.getUserid()));
        user.setName(userJpaEntity.getName());
        user.setEmail(userJpaEntity.getEmail());
        user.setSurname(userJpaEntity.getSurname());
        user.setPhonenumber(userJpaEntity.getPhonenumber());
        return user;
    }



    public List<Book> booksToDomainModels(List<BookJpaEntity> bookJpas)
    {
        return bookJpas.stream().map(this::bookToDomain).collect(Collectors.toList());
    }
}