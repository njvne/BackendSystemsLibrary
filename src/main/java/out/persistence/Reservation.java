package out.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "reservations")
public class Reservation
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO) @Column (name = "reservationid") private long reservationid;

    @OneToOne
    @JoinColumn(nullable = false, name = "userid") private UserJpaEntity user;

    @OneToOne
    @JoinColumn(nullable = false, name = "bookcopyid") private BookCopy bookcopy;

    @Column(nullable = false, name ="isactive") private boolean isactive;



    public long getReservationid() {return reservationid;}
    public void setReservationid(long reservationid) {this.reservationid = reservationid;}
    public BookCopy getBookcopy() {return bookcopy;}
    public void setBookcopy(BookCopy book) {this.bookcopy = book;}
    public UserJpaEntity getUser() {return user;}
    public void setUser(UserJpaEntity user) {this.user = user;}
    public boolean isIsactive() {return isactive;}
    public void setIsactive(boolean isactive) {this.isactive = isactive;}
}
