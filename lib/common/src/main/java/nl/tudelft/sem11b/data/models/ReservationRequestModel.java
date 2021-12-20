package nl.tudelft.sem11b.data.models;

import nl.tudelft.sem11b.data.ApiDateTime;

/**
 * Model used to create and update reservations.
 */
public class ReservationRequestModel {
    private transient Long roomId;
    private transient String title;
    private transient ApiDateTime since;
    private transient ApiDateTime until;
    private transient Long forUser;

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
}