package nl.tudelft.sem11b.data.models;

import java.sql.Timestamp;

public class ReservationModel {
    private final int roomId;
    private final Timestamp since;
    private final Timestamp until;
    private final String title;

    public int getRoomId() {
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

    public ReservationModel(int roomId, Timestamp since, Timestamp until, String title) {
        this.roomId = roomId;
        this.since = since;
        this.until = until;
        this.title = title;
    }
}
