package nl.tudelft.sem11b.admin.data;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import nl.tudelft.sem11b.data.Day;
import nl.tudelft.sem11b.data.models.ClosureModel;

@Embeddable
public class Closure {
    @Column(name = "reason")
    private String reason;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "year", column = @Column(name = "since_year")),
        @AttributeOverride(name = "day", column = @Column(name = "since_da"))
    })
    private Day since;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "year", column = @Column(name = "until_year")),
        @AttributeOverride(name = "day", column = @Column(name = "until_day"))
    })
    private Day until;

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

    private Closure() {
        // default constructor for entity materialization
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

    public ClosureModel toModel() {
        // TODO: unify this class and the model class (so move this class into common lib)
        return new ClosureModel(reason, since, until);
    }
}
