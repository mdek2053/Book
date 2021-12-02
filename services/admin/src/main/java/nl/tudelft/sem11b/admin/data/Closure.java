package nl.tudelft.sem11b.admin.data;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import nl.tudelft.sem11b.data.Day;

@Embeddable
public class Closure {
    @Column(name = "reason")
    private final String reason;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "year", column = @Column(name = "since_year")),
        @AttributeOverride(name = "day", column = @Column(name = "since_da"))
    })
    private final Day since;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "year", column = @Column(name = "until_year")),
        @AttributeOverride(name = "day", column = @Column(name = "until_day"))
    })
    private final Day until;

    public Closure(String reason, Day since) {
        this(reason, since, null);
    }

    public Closure(String reason, Day since, Day until) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Reason must be given!");
        }
        if (since == null) {
            throw new IllegalArgumentException("Since date cannot be null!");
        }

        this.reason = reason;
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
