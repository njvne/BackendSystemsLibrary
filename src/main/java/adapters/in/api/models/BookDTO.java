package adapters.in.api.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.DefaultValue;
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
    @DefaultValue("1")
    private int copyAmount;
    @DefaultValue("1")
    private int availableAmount;



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

    public int getCopyAmount()
    {
        return copyAmount;
    }

    public void setCopyAmount(int copyAmount)
    {
        this.copyAmount = copyAmount;
    }

    public int getAvailAmount()
    {
        return availableAmount;
    }

    public void setAvailAmount(int availableAmount)
    {
        this.availableAmount = availableAmount;
    }
}