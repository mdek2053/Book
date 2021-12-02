package nl.tudelft.sem11b.data.models;

import nl.tudelft.sem11b.data.Day;

/**
 * Represents a closure of a room.
 */
public class ClosureModel {
    private final String reason;
    private final Day since;
    private final Day until;

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
    public ClosureModel(String reason, Day since, Day until) {
        this.reason = reason;
        this.since = since;
        this.until = until;
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
    public Day getSince() {
        return since;
    }

    /**
     * Gets the ending date of the closure (if any).
     *
     * @return Ending date of the closure (or null)
     */
    public Day getUntil() {
        return until;
    }
}
