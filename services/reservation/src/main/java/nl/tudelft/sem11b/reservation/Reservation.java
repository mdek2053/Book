package nl.tudelft.sem11b.reservation;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "room_id")
    private long room_id;

    @Column(name = "user_id")
    private long user_id;

    @Column(name = "title")
    private String title;

    @Column(name = "since")
    private Date since;

    @Column(name = "until")
    private Date until;

    @Column(name = "cancel")
    private String cancel_reason;

    public Reservation() {
    }

    public Reservation(long id, long room_id, long user_id, String title, Date since, Date until, String cancel_reason) {
        this.id = id;
        this.room_id = room_id;
        this.user_id = user_id;
        this.title = title;
        this.since = since;
        this.until = until;
        this.cancel_reason = cancel_reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;

        Reservation that = (Reservation) o;

        if (id != that.id) return false;
        if (room_id != that.room_id) return false;
        if (user_id != that.user_id) return false;
        if (!title.equals(that.title)) return false;
        if (!since.equals(that.since)) return false;
        if (!until.equals(that.until)) return false;
        return cancel_reason != null ? cancel_reason.equals(that.cancel_reason) : that.cancel_reason == null;
    }
}
