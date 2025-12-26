package adapters.in.api.adapter;

import adapters.in.api.models.BookDTO;
import adapters.in.api.models.BorrowDTO;
import adapters.in.api.models.UserDTO;
import application.domain.models.*;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper
{
    public List<BookDTO> booksToApiModels(List<Book> books)
    {
        return books.stream().map(this::bookToApiModel).collect(Collectors.toList());
    }

    public BookDTO bookToApiModel(Book book)
    {
        final var bookDTO = new BookDTO();
        bookDTO.setId(book.getIsbn().getISBN());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setDescription(book.getDescription());
        bookDTO.setPublicationDate(book.getPublicationDate());
        bookDTO.setCopyAmount(book.getCopyAmount());
        bookDTO.setAvailAmount(book.getAvailableAmount());
        return bookDTO;
    }

    public Book bookDTOToDomainModel(BookDTO bookDTO)
    {
        final var book = new Book(new BookISBN(bookDTO.getId()));
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setDescription(bookDTO.getDescription());
        book.setPublicationDate(bookDTO.getPublicationDate());
        book.setCopyAmount(bookDTO.getCopyAmount());
        book.setAvailableAmount(bookDTO.getAvailAmount());
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

    public List<BorrowDTO> borrowsToApiModels(List<Borrow> borrows)
    {
        return borrows.stream().map(this::borrowToApiModel).collect(Collectors.toList());
    }

    public BorrowDTO borrowToApiModel(Borrow borrow)
    {
        final var borrowDTO = new BorrowDTO();
        borrowDTO.setUserid(borrow.getUserid());
        borrowDTO.setIsbn(borrow.getIsbn());
        borrowDTO.setBorrowdate(borrow.getBorrowdate());
        borrowDTO.setReturned(borrow.isReturned());
        return borrowDTO;
    }

    public Borrow borrowDTOToDomainModel(BorrowDTO borrowDTO)
    {
        final var borrow = new Borrow();
        borrow.setUserid(borrowDTO.getUserid());
        borrow.setIsbn(borrowDTO.getIsbn());
        borrow.setBorrowdate(borrowDTO.getBorrowdate());
        borrow.setReturned(borrowDTO.isReturned());
        return borrow;
    }
}