package adapters.out.persistence.models;
import jakarta.persistence.*;
import jakarta.ws.rs.DefaultValue;

@Entity
public class BookCopyJpaEntity
{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) @Column(name = "copyid") private long copyid;

    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(nullable = false, name = "isbn") private BookJpaEntity book;

    public boolean isRetired() {
        return isRetired;
    }

    public void setRetired(boolean retired) {
        isRetired = retired;
    }

    @DefaultValue("false")
    private boolean isRetired;



    public long getCopyid() {return copyid;}
    public void setCopyid(long copyid) {this.copyid = copyid;}
    public BookJpaEntity getBook() {return book;}
    public void setBook(BookJpaEntity book) {this.book = book;}
}