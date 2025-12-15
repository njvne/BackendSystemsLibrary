package adapters.in.api.adapter;

import adapters.in.api.models.BookDTO;
import adapters.in.api.models.BorrowDTO;
import adapters.in.api.models.UserDTO;
import application.domain.models.*;

public class Mapper
{
    public BookDTO bookToApiModel(Book book)
    {
        final var bookDTO = new BookDTO();
        bookDTO.setId(book.getIsbn().getISBN());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setDescription(book.getDescription());
        bookDTO.setPublicationDate(book.getPublicationDate());
        return bookDTO;
    }

    public Book bookDTOToDomainModel(BookDTO bookDTO)
    {
        final var book = new Book(new BookISBN(bookDTO.getId()));
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setDescription(bookDTO.getDescription());
        book.setPublicationDate(bookDTO.getPublicationDate());
        return book;
    }

    public UserDTO userToApiModel(User user)
    {
        final var userDTO = new UserDTO();
        userDTO.setId(user.getUserid().getId());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhonenumber(user.getPhonenumber());
        userDTO.setBirthdate(user.getBirthdate());
        return userDTO;
    }

    public User userToDomainModel(UserDTO userDTO)
    {
        final var user = new User();
        user.setUserid(new UserID(userDTO.getId()));
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setEmail(userDTO.getEmail());
        user.setPhonenumber(userDTO.getPhonenumber());
        user.setBirthdate(userDTO.getBirthdate());
        return user;
    }
}