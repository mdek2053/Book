package nl.tudelft.sem11b.data.models;

import java.sql.Timestamp;

public class ReservationModel {
    private final long roomId;
    private final Timestamp since;
    private final Timestamp until;
    private final String title;

    public long getRoomId() {
        return roomId;
    }

    public Timestamp getSince() {
        return since;
    }

    public Timestamp getUntil() {
        return until;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Initiates ReservationModel class.
     * @param roomId unique ID of the room the reservation is in
     * @param since start time of reservation
     * @param until end time of reservation
     * @param title title of reservation
     */
    public ReservationModel(long roomId, Timestamp since, Timestamp until, String title) {
        this.roomId = roomId;
        this.since = since;
        this.until = until;
        this.title = title;
    }
}
