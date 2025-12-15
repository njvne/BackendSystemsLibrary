package adapters.out.persistence.models;
import jakarta.persistence.*;


@Entity
public class BookJpaEntity
{
    @Id
    private long isbn;

    private long title;
    private String author;
    private String description;
    private String dateofpublication;



    public long getIsbn() {return isbn;}
    public void setIsbn(long isbn) {this.isbn = isbn;}
    public long getTitle() {return title;}
    public void setTitle(long title) {this.title = title;}
    public String getAuthor() {return author;}
    public void setAuthor(String author) {this.author = author;}
    public String getDateofpublication() {return dateofpublication;}
    public void setDateofpublication(String dateofpublication) {this.dateofpublication = dateofpublication;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
}