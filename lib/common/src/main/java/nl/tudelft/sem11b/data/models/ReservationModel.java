package nl.tudelft.sem11b.data.models;

import java.util.Objects;

import nl.tudelft.sem11b.data.ApiDateTime;

/**
 * Holds all information about a single reservation.
 */
public class ReservationModel {
    private long roomId;
    private ApiDateTime since;
    private ApiDateTime until;
    private String title;

    /**
     * Gets the unique numeric identifier of the room the reservation takes place in.
     *
     * @return ID of the reservation's room
     */
    public long getRoomId() {
        return roomId;
    }

    /**
     * Gets the beginning date and time of the reservation.
     *
     * @return Starting date and time
     */
    public ApiDateTime getSince() {
        return since;
    }

    /**
     * Gets the ending date and time of the reservation.
     *
     * @return Ending date and time
     */
    public ApiDateTime getUntil() {
        return until;
    }

    /**
     * Gets the title of the reservation.
     *
     * @return Title of reservation
     */
    public String getTitle() {
        return title;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public void setSince(ApiDateTime since) {
        this.since = since;
    }

    public void setUntil(ApiDateTime until) {
        this.until = until;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Initiates ReservationModel class.
     *
     * @param roomId unique ID of the room the reservation is in
     * @param since  start time of reservation
     * @param until  end time of reservation
     * @param title  title of reservation
     */
    public ReservationModel(long roomId, ApiDateTime since, ApiDateTime until, String title) {
        this.roomId = roomId;
        this.since = since;
        this.until = until;
        this.title = title;
    }

    private ReservationModel() {
        // default constructor for model materialization
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