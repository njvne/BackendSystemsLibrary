package adapters.out.persistence.models;
import jakarta.persistence.*;

@Entity
public class BookCopyJpaEntity
{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) @Column(name = "copyid") private long copyid;

    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(nullable = false, name = "isbn") private BookJpaEntity book;



    public long getCopyid() {return copyid;}
    public void setCopyid(long copyid) {this.copyid = copyid;}
    public BookJpaEntity getBook() {return book;}
    public void setBook(BookJpaEntity book) {this.book = book;}
}