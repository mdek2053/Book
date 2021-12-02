package nl.tudelft.sem11b.reservation.entity;

import java.io.Serializable;

public class ReservationResponse implements Serializable {
    Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReservationResponse() {
    }

    public ReservationResponse(Long id) {
        this.id = id;
    }
}