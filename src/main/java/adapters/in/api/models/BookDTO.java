package adapters.in.api.models;

import application.domain.models.BookISBN;
import jakarta.validation.constraints.NotBlank;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Date;

@XmlRootElement(name = "book")
public class BookDTO extends AbstractDataTransferObject
{
    @NotBlank (message = "Book must have a title")
    private String title;
    @NotBlank (message = "Book must have an author")
    private String author;
    @NotBlank (message = "Book must have a description")
    private String description;
    @NotBlank (message = "Book must have a publication date")
    private Date publicationDate;



    public BookDTO()
    {
        super();
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Date getPublicationDate()
    {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate)
    {
        this.publicationDate = publicationDate;
    }
}