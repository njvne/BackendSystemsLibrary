package application.domain;

import application.domain.models.Book;
import application.domain.models.BookISBN;
import com.github.javafaker.Faker;

public class BookDataFaker
{
    private final Faker faker = new Faker();

    public Book createModel()
    {
        //todo
        return new Book(new BookISBN());
    }
}