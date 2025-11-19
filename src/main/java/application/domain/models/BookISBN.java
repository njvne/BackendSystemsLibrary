package application.domain.models;

public class BookISBN
{
    private long ISBN;



    //constructors
    public BookISBN()
    {

    }
    public BookISBN(long ISBN)
    {
        this.ISBN = ISBN;
    }



    //getters and setters
    public long getISBN() { return ISBN; }

    public void setISBN(long ISBN) { this.ISBN = ISBN; }
}