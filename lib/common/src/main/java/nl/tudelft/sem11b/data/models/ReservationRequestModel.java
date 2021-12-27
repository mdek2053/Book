package nl.tudelft.sem11b.data.models;

import nl.tudelft.sem11b.data.ApiDateTime;

import java.util.Objects;

/**
 * Model used to create and update reservations.
 */
public class ReservationRequestModel {
    private Long roomId;
    private String title;
    private ApiDateTime since;
    private ApiDateTime until;
    private Long forUser;

    /**
     * Gets the unique numeric identifier of the room the reservation takes place in.
     *
     * @return ID of reserved room
     */
    public Long getRoomId() {
        return roomId;
    }

    /**
     * Gets the title of the reservation.
     *
     * @return Title of reservation
     */
    public String getTitle() {
        return title;
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
     * Gets the unique numeric identifier of the user on behalf of whom the reservation is being
     * made.
     *
     * @return ID of the delegated user
     */
    public Long getForUser() {
        return forUser;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSince(ApiDateTime since) {
        this.since = since;
    }

    public void setUntil(ApiDateTime until) {
        this.until = until;
    }

    public void setForUser(Long forUser) {
        this.forUser = forUser;
    }

    /**
     * Checks if a reservation entity has all mandatory fields filled in.
     *
     * @return true if valid entity, false otherwise
     */
    public boolean validate() {
        return roomId != null
            && title != null && !title.isBlank()
            && since != null
            && until != null;
    }

    private ReservationRequestModel() {
        // default constructor for model materialization
    }

    /**
     * Instantiates the {@link ReservationRequestModel} class.
     *
     * @param roomId  id of the room the reservation is for
     * @param title   title of the reservation
     * @param since   start time of the reservation
     * @param until   end time of the reservation
     * @param forUser optinally, can specify for which user the reservation is made
     */
    public ReservationRequestModel(Long roomId, String title, ApiDateTime since, ApiDateTime until,
                                   Long forUser) {
        this.roomId = roomId;
        this.title = title;
        this.since = since;
        this.until = until;
        this.forUser = forUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationRequestModel that = (ReservationRequestModel) o;
        return roomId.equals(that.roomId) && title.equals(that.title) && since.equals(that.since) && until.equals(that.until) && forUser.equals(that.forUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, title, since, until, forUser);
    }

    @Override
    public String toString() {
        return "ReservationRequestModel{" +
                "roomId=" + roomId +
                ", title='" + title + '\'' +
                ", since=" + since +
                ", until=" + until +
                ", forUser=" + forUser +
                '}';
    }
}