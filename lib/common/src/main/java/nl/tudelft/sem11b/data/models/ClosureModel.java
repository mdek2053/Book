package nl.tudelft.sem11b.data.models;

import java.util.Objects;

import nl.tudelft.sem11b.data.ApiDate;

/**
 * Represents a closure of a room.
 */
public class ClosureModel {
    private transient String reason;
    private transient ApiDate since;
    private transient ApiDate until;

    /**
     * Instantiates the {@link ClosureModel} class.
     *
     * @param reason Reason for closure
     */
    public ClosureModel(String reason) {
        this(reason, null, null);
    }

    /**
     * Instantiates the {@link ClosureModel} class.
     *
     * @param reason Reason for closure
     * @param since  Time when closure begins
     * @param until  Time when closure ends
     */
    public ClosureModel(String reason, ApiDate since, ApiDate until) {
        this.reason = reason;
        this.since = since;
        this.until = until;
    }

    private ClosureModel() {
        // default constructor for model materialization
    }

    /**
     * Gets the reason for room closure.
     *
     * @return Reason for closure
     */
    public String getReason() {
        return reason;
    }

    /**
     * Gets the beginning date of the closure (if any).
     *
     * @return Beginning date of the closure (or null)
     */
    public ApiDate getSince() {
        return since;
    }

    /**
     * Gets the ending date of the closure (if any).
     *
     * @return Ending date of the closure (or null)
     */
    public ApiDate getUntil() {
        return until;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClosureModel)) {
            return false;
        }

        ClosureModel that = (ClosureModel) o;

        if (!Objects.equals(reason, that.reason)) {
            return false;
        }
        if (!Objects.equals(since, that.since)) {
            return false;
        }
        return Objects.equals(until, that.until);
    }

    @Override
    public int hashCode() {
        int result = reason != null ? reason.hashCode() : 0;
        result = 31 * result + (since != null ? since.hashCode() : 0);
        result = 31 * result + (until != null ? until.hashCode() : 0);
        return result;
    }
}
