package nl.tudelft.sem11b.data.models;

import nl.tudelft.sem11b.data.Day;

public class ClosureModel {
    private final String reason;
    private final Day since;
    private final Day until;

    public ClosureModel(String reason) {
        this(reason, null, null);
    }

    public ClosureModel(String reason, Day since, Day until) {
        this.reason =reason;
        this.since = since;
        this.until = until;
    }

    public String getReason() {
        return reason;
    }

    public Day getSince() {
        return since;
    }

    public Day getUntil() {
        return until;
    }
}
