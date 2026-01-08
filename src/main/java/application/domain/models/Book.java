package application.domain.models;


import jakarta.ws.rs.DefaultValue;

public class Book
{
    private BookISBN isbn;
    private String title;
    private String author;
    private String description;
    private long copyAmount;
    private int availableAmount;



    //constructor
    public Book(BookISBN isbn)
    {
        this.isbn = new BookISBN();
    }

    public Book()
    {

    }



    //getters and setters
    public BookISBN getIsbn() { return isbn; }

    public void setIsbn(BookISBN isbn) { this.isbn = isbn; }

    public String getTitle() {
    return title;
}

    public void setTitle(String title) {
    this.title = title;
}

    public String getAuthor() {
    return author;
}

    public void setAuthor(String author) {
    this.author = author;
}

    public String getDescription() {
    return description;
}

    public void setDescription(String description) {
    this.description = description;
}

    public long getCopyAmount() {return copyAmount;}

    public void setCopyAmount(long copyAmount) {this.copyAmount = copyAmount;}

    public int getAvailableAmount() {return availableAmount;}

    public void setAvailableAmount(int availableAmount) {this.availableAmount = availableAmount;}
}