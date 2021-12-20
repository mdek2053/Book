package nl.tudelft.sem11b.data.models;

import java.util.Objects;

import nl.tudelft.sem11b.data.exceptions.EntityNotFound;

/**
 * Model used to create fault reports.
 */
public class FaultRequestModel {
    private transient long reservationId;
    private transient String description;

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

    public FaultRequestModel() {
    }

    /**
     * Instantiates the {@link FaultRequestModel} class.
     *
     * @param reservationId ID of the reservation during which the fault was observed
     * @param description   description of the fault
     */
    public FaultRequestModel(long reservationId, String description) {
        this.reservationId = reservationId;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FaultRequestModel)) {
            return false;
        }

        FaultRequestModel that = (FaultRequestModel) o;

        if (reservationId != that.reservationId) {
            return false;
        }
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = (int) (reservationId ^ (reservationId >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
