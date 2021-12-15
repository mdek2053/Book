package nl.tudelft.sem11b.data.models;

/**
 * Model used to create fault reports.
 */
public class FaultRequestModel {
    private long reservationId;
    private String description;

    /**
     * Gets the ID of the reservation during which the fault was discovered.
     *
     * @return Reservation ID
     */
    public long getReservationId() {
        return reservationId;
    }

    /**
     * Gets the description of the fault.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    private FaultRequestModel() {
        // default constructor for model materialization
    }
}
