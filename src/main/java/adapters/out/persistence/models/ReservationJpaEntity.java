package adapters.out.persistence.models;

import jakarta.persistence.*;

@Entity
@Table(name = "reservations")
public class ReservationJpaEntity
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO) @Column (name = "reservationid") private long reservationid;

    @OneToOne
    @JoinColumn(nullable = false, name = "userid") private UserJpaEntity user;

    @OneToOne
    @JoinColumn(nullable = false, name = "bookcopyid") private BookCopyJpaEntity bookcopy;

    @Column(nullable = false, name ="isactive") private boolean isactive;



    public long getReservationid() {return reservationid;}
    public void setReservationid(long reservationid) {this.reservationid = reservationid;}
    public BookCopyJpaEntity getBookcopy() {return bookcopy;}
    public void setBookcopy(BookCopyJpaEntity book) {this.bookcopy = book;}
    public UserJpaEntity getUser() {return user;}
    public void setUser(UserJpaEntity user) {this.user = user;}
    public boolean isIsactive() {return isactive;}
    public void setIsactive(boolean isactive) {this.isactive = isactive;}
}