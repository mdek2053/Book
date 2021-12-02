package nl.tudelft.sem11b.reservation.entity;

public class ReservationRequest {
    Long room_id;
    String title;
    String since;
    String until;
    Long for_user;

    public Long getRoom_id() {
        return room_id;
    }

    public String getTitle() {
        return title;
    }

    public String getSince() {
        return since;
    }

    public String getUntil() {
        return until;
    }

    public Long getFor_user() {
        return for_user;
    }

    public boolean validate() {
        return room_id != null &&
                title != null && !title.isBlank() &&
                since != null && !since.isBlank() &&
                until != null && !until.isBlank();
    }

    public ReservationRequest() {
    }

    public ReservationRequest(Long room_id, String title, String since, String until, Long for_user) {
        this.room_id = room_id;
        this.title = title;
        this.since = since;
        this.until = until;
        this.for_user = for_user;
    }
}