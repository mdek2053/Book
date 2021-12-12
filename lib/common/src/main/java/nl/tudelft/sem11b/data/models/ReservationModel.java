package nl.tudelft.sem11b.data.models;

import java.util.Objects;

import nl.tudelft.sem11b.data.ApiDateTime;

public class ReservationModel {
    private long roomId;
    private ApiDateTime since;
    private ApiDateTime until;
    private String title;

    public long getRoomId() {
        return roomId;
    }

    public ApiDateTime getSince() {
        return since;
    }

    public ApiDateTime getUntil() {
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
    public ReservationModel(long roomId, ApiDateTime since, ApiDateTime until, String title) {
        this.roomId = roomId;
        this.since = since;
        this.until = until;
        this.title = title;
    }

    private ReservationModel() {

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationModel that = (ReservationModel) o;
        return roomId == that.roomId
                && Objects.equals(since, that.since)
                && Objects.equals(until, that.until)
                && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, since, until, title);
    }
}