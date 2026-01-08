package adapters.out.persistence.models;
import jakarta.persistence.*;

import java.io.Serializable;


@Entity
public class BookJpaEntity implements Serializable
{
    @Id private long isbn;
    private String title;
    private String author;
    private String description;



    public long getIsbn() {return isbn;}
    public void setIsbn(long isbn) {this.isbn = isbn;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getAuthor() {return author;}
    public void setAuthor(String author) {this.author = author;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
}