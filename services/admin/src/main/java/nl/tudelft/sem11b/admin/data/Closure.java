package nl.tudelft.sem11b.admin.data;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Closure {
    @Column(name = "reason")
    private final String reason;
    @Column(name = "since")
    private final Date since;
    @Column(name = "until")
    private final Date until;

    public Closure(String reason, Date since) {
        this(reason, since, null);
    }

    public Closure(String reason, Date since, Date until) {
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

    public Date getSince() {
        return since;
    }

    public Date getUntil() {
        return until;
    }
}
