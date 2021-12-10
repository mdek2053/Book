package nl.tudelft.sem11b.reservation.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import nl.tudelft.sem11b.data.models.ReservationModel;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "room_id")
    private long roomId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "title")
    private String title;

    @Column(name = "since")
    private Timestamp since;

    @Column(name = "until")
    private Timestamp until;

    @Column(name = "cancel")
    private String cancelReason;

    public long getId() {
        return id;
    }

    public long getRoomId() {
        return roomId;
    }

    public long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public Timestamp getSince() {
        return since;
    }

    public Timestamp getUntil() {
        return until;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public Reservation() {
    }

    /**
     * Creates a Reservation object.
     * @param id the reservation's unique identifying number
     * @param roomId id of the room the reservation is in
     * @param userId id of the user the reservation is for
     * @param title title of the reservation
     * @param since start time of reservation
     * @param until end time of reservation
     * @param cancelReason null if not cancelled, non-empty otherwise
     */
    public Reservation(long id, long roomId, long userId, String title,
                       Timestamp since, Timestamp until, String cancelReason) {
        this.id = id;
        this.roomId = roomId;
        this.userId = userId;
        this.title = title;
        this.since = since;
        this.until = until;
        this.cancelReason = cancelReason;
    }

    /**
     * Creates a Reservation object.
     * @param roomId id of the room the reservation is in
     * @param userId id of the user the reservation is for
     * @param title title of the reservation
     * @param since start time of reservation
     * @param until end time of reservation
     */
    public Reservation(long roomId, long userId, String title, Timestamp since, Timestamp until) {
        this.roomId = roomId;
        this.userId = userId;
        this.title = title;
        this.since = since;
        this.until = until;
        this.cancelReason = null;
    }

    public ReservationModel toModel() {
        // TODO: ApiDateTime
        return new ReservationModel(id, since.toString(), until.toString(), title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation)) {
            return false;
        }

        Reservation that = (Reservation) o;

        if (id != that.id) {
            return false;
        }
        if (roomId != that.roomId) {
            return false;
        }
        if (userId != that.userId) {
            return false;
        }
        if (!title.equals(that.title)) {
            return false;
        }
        if (!since.equals(that.since)) {
            return false;
        }
        if (!until.equals(that.until)) {
            return false;
        }
        return cancelReason != null
                ? cancelReason.equals(that.cancelReason) : that.cancelReason == null;
    }

    @Override
    public String toString() {
        return "Reservation{"
                + "id=" + id
                + ", room_id=" + roomId
                + ", user_id=" + userId
                + ", title='" + title + '\''
                + ", since=" + since
                + ", until=" + until
                + ", cancel_reason='" + cancelReason + '\''
                + '}';
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSince(Timestamp since) {
        this.since = since;
    }

    public void setUntil(Timestamp until) {
        this.until = until;
    }
}
