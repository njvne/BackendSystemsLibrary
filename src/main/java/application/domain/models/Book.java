package application.domain.models;


import java.util.Date;

public class Book
{
    private BookISBN isbn;
    private String title;
    private String author;
    private String description;
    private Date publicationDate;
    private int copyAmount;
    private int availableAmount;



    //constructor
    public Book(BookISBN isbn)
    {
        this.isbn = new BookISBN();
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

    public Date getPublicationDate() {
    return publicationDate;
}

    public void setPublicationDate(Date publicationDate) {
    this.publicationDate = publicationDate;
}

    public int getCopyAmount() {return copyAmount;}

    public void setCopyAmount(int copyAmount) {this.copyAmount = copyAmount;}

    public int getAvailableAmount() {return availableAmount;}

    public void setAvailableAmount(int availableAmount) {this.availableAmount = availableAmount;}
}