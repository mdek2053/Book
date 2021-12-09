package nl.tudelft.sem11b.data.models;

import nl.tudelft.sem11b.data.ApiDateTime;

public class ReservationRequestModel {
    private Long roomId;
    private String title;
    private ApiDateTime since;
    private ApiDateTime until;
    private Long forUser;

    public Long getRoomId() {
        return roomId;
    }

    public String getTitle() {
        return title;
    }

    public ApiDateTime getSince() {
        return since;
    }

    public ApiDateTime getUntil() {
        return until;
    }

    public Long getForUser() {
        return forUser;
    }

    /**
     * Checks if a reservation entity has all mandatory fields filled in.
     * @return true if valid entity, false otherwise
     */
    public boolean validate() {
        return roomId != null
                && title != null && !title.isBlank()
                && since != null
                && until != null;
    }

    private ReservationRequestModel() {
    }

    /**
     * Creates a ReservationRequest object.
     * @param roomId id of the room the reservation is for
     * @param title title of the reservation
     * @param since start time of the reservation
     * @param until end time of the reservation
     * @param forUser optinally, can specify for which user the reservation is made
     */
    public ReservationRequestModel(Long roomId, String title, ApiDateTime since, ApiDateTime until, Long forUser) {
        this.roomId = roomId;
        this.title = title;
        this.since = since;
        this.until = until;
        this.forUser = forUser;
    }
}