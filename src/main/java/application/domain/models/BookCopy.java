package application.domain.models;

public class BookCopy
{
    private CopyID copyid;
    private String BookISBN;



    //constructor
    public BookCopy()
    {
        this.copyid = new CopyID();
    }



    //getters and setters
    public CopyID getCopyid() { return copyid; }

    public void setCopyid(CopyID copyid) { this.copyid = copyid; }

    public String getBookISBN() { return BookISBN; }

    public void setBookISBN(String bookISBN) { BookISBN = bookISBN; }
}